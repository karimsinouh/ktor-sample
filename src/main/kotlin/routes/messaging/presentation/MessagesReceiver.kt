package com.example.routes.messaging.presentation

import com.example.core.model.failureResponse
import com.example.core.model.getCorrectPhoneNumberFormat
import com.example.core.model.successResponse
import com.example.routes.messaging.data.GenerateAIResponse
import com.example.routes.messaging.model.ChatRepository
import com.example.routes.messaging.model.WhatsAppBusinessAccount
import com.example.routes.users.model.UsersRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

/**
 * Receives messages from Whatsapp API and.
 * Generates Messages from OpenAI.
 * Stores Messages from both the user and AI in the database.
 * Sends a message back to routes.users via Whatsapp API.
 * */
fun Routing.messagesReceiver(
    repo: ChatRepository,
)=post("/messages/messagesReceiver") {

    try {


        //receive the user message and store it in the database
        val requestBody = call.receive<WhatsAppBusinessAccount>()
        val message= extractMessageAndSender(requestBody)

        println("Received a message")
        println(requestBody.toString())

        val sender = getCorrectPhoneNumberFormat(message.first)
        val text = message.second

        repo.messages.insert("user",sender, text)

        //get the last 10 messages from this conversation from the database
        val messages=repo.messages.getLastMessages(sender)


        //generate AI response for the user message
        repo.generateAIResponse(
            messages = messages,
            onSuccess = {aiResponse->

                println("AI responded a message")
                println(aiResponse)

                //store the AI response in the database
                repo.messages.insert(GenerateAIResponse.AIMessage.ROLE_ASSISTANT,sender,aiResponse)

                //send the AI response back to the user via WhatsApp API
                repo.sendWhatsappMessage(
                    phoneNumber = sender,
                    message=aiResponse,
                    onSuccess = ::successResponse,
                    onFailure = ::failureResponse
                )


            },
            onFailure = ::failureResponse
        )

    } catch (e: Exception) {
        println(e.message?:"Message receiving failed")
        failureResponse(e.message?:"Message receiving failed")
    }

}

fun extractMessageAndSender(response: WhatsAppBusinessAccount): Pair<String, String> {
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

    val verificationToken="karimsinouh"
    val challenge=call.request.queryParameters["hub.challenge"]
    val receivedVerificationToken=call.request.queryParameters["hub.verify_token"]

    if (receivedVerificationToken==verificationToken){
        call.respondText(challenge?:"",ContentType.Text.Plain)
        println("Success. token verification: $receivedVerificationToken, challenge: $challenge")
    }else{
        call.respond(HttpStatusCode.Forbidden,"Verification Failed")
        println("Failure. token verification: $receivedVerificationToken, challenge: $challenge")
    }

}