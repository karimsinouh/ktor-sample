package com.example.routes.messaging.presentation

import com.example.core.ConfigureAIModel
import com.example.core.Constants
import com.example.core.model.failureResponse
import com.example.core.model.getCorrectPhoneNumberFormat
import com.example.core.model.successResponse
import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.domain.ChatRepository
import com.example.routes.messaging.model.WhatsAppMessageResponse
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.UserModel
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.ObjectInputFilter.Config

/**
 * Receives messages from Whatsapp API and.
 * Generates Messages from OpenAI.
 * Stores Messages from both the user and AI in the database.
 * Sends a message back to routes.users via Whatsapp API.
 * */
fun Routing.messagesReceiver(
    repo: ChatRepository,
    usersRepository: UsersRepository,
)=post("/messages/messagesReceiver") {

    try {


        //receive the user message and store it in the database
        val requestBody = call.receive<WhatsAppMessageResponse>()
        val message= extractMessageAndSender(requestBody)
        val clientPhoneNumber = getCorrectPhoneNumberFormat(message.first)
        val text = message.second
        println("-> message received -> $text from $clientPhoneNumber ")


        //store the client message in database
        repo.messages.insert(AIMessage.ROLE_USER,text, clientPhoneNumber)

        //get the last 10 messages from this conversation from the database
        val messages=repo.messages.getLastMessages(clientPhoneNumber)


        //generate AI response for the user message
        repo.generateAIResponse(
            clientPhoneNumber = clientPhoneNumber,
            messages = messages,
            onSuccess = {aiResponse,user->

                //store the AI response in the database
                repo.messages.insert(AIMessage.ROLE_ASSISTANT,aiResponse.user_message?:"",clientPhoneNumber)

                println("ACTION: ${aiResponse.action?:""}")
                when(aiResponse.action){

                    //normal chat message
                    ConfigureAIModel.Actions.NORMAL_CHAT_MESSAGE->{
                        //send the AI response back to the user via WhatsApp API
                        repo.sendWhatsappMessage(
                            phoneNumber = clientPhoneNumber,
                            message=aiResponse.user_message?:"",
                            onSuccess = ::successResponse,
                            onFailure = ::failureResponse
                        )
                    }

                    //save client details in the database
                    ConfigureAIModel.Actions.SAVE_CLIENT_DETAILS->{
                        //send the AI response back to the user via WhatsApp API
                        usersRepository.insertFromAIResponse(
                            clientPhoneNumber,
                            aiResponse,
                            onSuccess = {
                                repo.sendWhatsappMessage(
                                    phoneNumber = clientPhoneNumber,
                                    message=aiResponse.user_message?:"",
                                    onSuccess = ::successResponse,
                                    onFailure = ::failureResponse
                                )
                            },
                            onFailure = ::failureResponse
                        )

                    }

                    ConfigureAIModel.Actions.RETRIEVE_CLIENT_INFORMATION->{
                        repo.sendWhatsappMessage(
                            phoneNumber = clientPhoneNumber,
                            message=aiResponse.user_message?:"",
                            onSuccess = ::successResponse,
                            onFailure = ::failureResponse
                        )
                    }

                }

            },
            onFailure = {
                //send a message to the user to let him know that server encountered an error
                repo.sendWhatsappMessage(clientPhoneNumber, "We're really sorry. We encountered an unexpected error. Please try again later. Thank you.", {}, ::failureResponse)
                failureResponse(it)
                println("/MessagesReceiver $it")
            }
        )

    } catch (e: Exception) {
        println(e.message?:"Message receiving failed")
        failureResponse(e.message?:"Message receiving failed")
    }

}

fun extractMessageAndSender(response: WhatsAppMessageResponse): Pair<String, String> {
    // Traverse the nested structure
    for (entry in response.entry) {
        for (change in entry.changes) {
            if (change.field == "messages") {
                for (message in change.value.messages) {
                    val sender = message.from
                    val messageBody = message.text.body
                    return Pair(sender, messageBody)
                }
            }
        }
    }
    throw IllegalStateException("No message or sender found")
}

fun Routing.verifyToken()=get("/messages/messagesReceiver") {

    val challenge=call.request.queryParameters["hub.challenge"]
    val receivedVerificationToken=call.request.queryParameters["hub.verify_token"]

    if (receivedVerificationToken==Constants.WHATSAPP_VERIFICATION_TOKEN){
        call.respondText(challenge?:"",ContentType.Text.Plain)
        println("Success. token verification: $receivedVerificationToken, challenge: $challenge")
    }else{
        call.respond(HttpStatusCode.Forbidden,"Verification Failed")
        println("Failure. token verification: $receivedVerificationToken, challenge: $challenge")
    }

}