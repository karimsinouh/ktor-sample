package com.example.routes.messaging.presentation

import com.example.core.AgentCore
import com.example.core.Env
import com.example.core.model.getCorrectPhoneNumberFormat
import com.example.di.DIModule
import features.messaging.data.ChatRepository
import com.example.routes.messaging.model.WhatsAppMessageResponse
import features.users.domain.UsersRepository
import features.messaging.useCase.ProcessIncomingWhatsappMessages
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
    processIncomingWhatsappMessages: ProcessIncomingWhatsappMessages,
)=post("/messages/messagesReceiver") {


    //receive the user message
    val requestBody = call.receive<WhatsAppMessageResponse>()
    call.respond(HttpStatusCode.OK)

    val phoneAndMessage= extractMessageAndSender(requestBody)
    val clientPhoneNumber = getCorrectPhoneNumberFormat(phoneAndMessage.first)
    val message = phoneAndMessage.second


    processIncomingWhatsappMessages(clientPhoneNumber,message)

}

fun extractMessageAndSender(response: WhatsAppMessageResponse): Pair<String, String> {
    val message = response.entry
        .flatMap { it.changes }
        .map { it.value.messages }
        .firstOrNull { it.isNotEmpty() }
        ?.firstOrNull()
        ?: throw IllegalStateException("No valid message found in webhook payload")

    return message.from to message.text.body
}

fun Routing.verifyTokenKoog()=get("/messages/messagesReceiver") {

    val challenge=call.request.queryParameters["hub.challenge"]
    val receivedVerificationToken=call.request.queryParameters["hub.verify_token"]

    if (receivedVerificationToken==Env.WHATSAPP_VERIFICATION_TOKEN){
        call.respondText(challenge?:"",ContentType.Text.Plain)
        println("Success. token verification: $receivedVerificationToken, challenge: $challenge")
    }else{
        call.respond(HttpStatusCode.Forbidden,"Verification Failed")
        println("Failure. token verification: $receivedVerificationToken, challenge: $challenge")
    }

}