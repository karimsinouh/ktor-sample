package com.example.routes.messaging.presentation

import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.agents.ext.agent.reActStrategy
import ai.koog.ktor.aiAgent
import ai.koog.ktor.llm
import ai.koog.prompt.dsl.prompt
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.clients.openai.OpenAIModels
import com.example.core.ConfigureAIModel
import com.example.core.Constants
import com.example.core.model.failureResponse
import com.example.core.model.getCorrectPhoneNumberFormat
import com.example.core.model.successResponse
import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.domain.ChatRepository
import com.example.routes.messaging.model.StructuredResponseBody
import com.example.routes.messaging.model.WhatsAppMessageResponse
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.domain.UsersToolSet
import com.example.routes.users.model.User
import com.example.routes.users.model.UserModel
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.ObjectInputFilter.Config
import kotlin.reflect.KType

/**
 * Receives messages from Whatsapp API and.
 * Generates Messages from OpenAI.
 * Stores Messages from both the user and AI in the database.
 * Sends a message back to routes.users via Whatsapp API.
 * */
fun Routing.messagesReceiverKoog(
    repo: ChatRepository,
    usersRepository: UsersRepository,
)=post("/messages/messagesReceiver") {

    try {


        //receive the user message and store it in the database
        val requestBody = call.receive<WhatsAppMessageResponse>()
        call.respond(HttpStatusCode.OK)

        val message= extractMessageAndSenderKoog(requestBody)
        val clientPhoneNumber = getCorrectPhoneNumberFormat(message.first)
        val text = message.second
        println("### message received -> $text from $clientPhoneNumber ")


        //get the last 10 messages from this conversation from the database
        val history=repo.messages.getLastMessages(clientPhoneNumber).reversed()

        //store the client message in database
        repo.messages.insert(AIMessage.ROLE_USER,text, clientPhoneNumber)


//        // 1. Format History clearly with dividers
//        val historyText = history.joinToString(separator = "\n") { msg ->
//            val role = if (msg.sender == AIMessage.ROLE_USER) "User" else "Assistant"
//            "- $role: ${msg.message}"
//        }
//
//        //format the prompt input properly
//        val promptInput="""
//
//            # CONTEXT
//            The user's phone number is: ${'$'}clientPhoneNumber
//
//            # CONVERSATION HISTORY
//            $historyText
//
//            # CURRENT REQUEST
//            User: $text
//
//        """.trimIndent()

        val aiResponse=aiAgent(
            model = GoogleModels.Gemini2_0Flash,
            strategy = reActStrategy(),
            tools = ToolRegistry{
                tools(UsersToolSet(usersRepository))
            }
        ).run {
            prompt("chat"){
                system("this client's phone number is: $clientPhoneNumber")
                history.forEach { message->
                    when(message.sender){
                        AIMessage.ROLE_USER->user(message.message?:"")
                        AIMessage.ROLE_ASSISTANT->assistant(message.message?:"")
                    }
                }
                user(text)
            }
        }.messages.joinToString(separator = " ") { it.content }

        println("### AI response ->  $aiResponse ")


        //store the AI response in the database
        repo.messages.insert(
            sender = AIMessage.ROLE_ASSISTANT,
            message = aiResponse,
            userPhoneNumber = clientPhoneNumber
        )

        //send the AI response back to user via whatsapp
        repo.sendWhatsappMessage(
            phoneNumber = clientPhoneNumber,
            message=aiResponse,
            onSuccess = ::successResponse,
            onFailure = ::failureResponse
        )


    } catch (e: Exception) {
        println(e.message?:"Message receiving failed")
        failureResponse(e.message?:"Message receiving failed")
    }

}

fun extractMessageAndSenderKoog(response: WhatsAppMessageResponse): Pair<String, String> {
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

fun Routing.verifyTokenKoog()=get("/messages/messagesReceiver") {

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