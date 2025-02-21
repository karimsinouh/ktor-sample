package com.example.routes.messaging.presentation

import com.example.core.errorResponse
import com.example.core.successResponse
import com.example.routes.messaging.data.GenerateAIResponse
import com.example.routes.messaging.model.ChatRepository
import com.example.routes.messaging.model.MessageModel
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
    repo: ChatRepository
)=post("/messagesReceiver") {

    try {


        //receive the user message and store it in the database
        val message = call.receive<MessageModel>()
        repo.messages.insert("user", message.phoneNumber, message.message)

        //get the last 10 messages from this conversation from the database
        val messages=repo.messages.getLastMessages(message.phoneNumber)

        //generate AI response for the user message
        repo.generateAIResponse(
            user=null,
            messages = messages,
            onSuccess = {aiResponse->

                //store the AI response in the database
                repo.messages.insert(GenerateAIResponse.AIMessage.ROLE_ASSISTANT,message.phoneNumber,aiResponse)

                //send the AI response back to the user via WhatsApp API
                repo.sendWhatsappMessage(
                    phoneNumber = message.phoneNumber,
                    message=aiResponse,
                    onSuccess = ::successResponse,
                    onFailure = ::errorResponse
                )

            },
            onFailure = ::errorResponse
        )

    } catch (e: Exception) {
        errorResponse("Failed to insert message: ${e.message}")
    }

}