package com.example.routes.messaging.presentation

import com.example.routes.messaging.model.MessagesRepository
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.getMessages(messagesRepository: MessagesRepository){

    // Endpoint to get messages by phone number
    get("/get/{phoneNumber}") {

        val phoneNumber = call.parameters["phoneNumber"]
        if (phoneNumber.isNullOrBlank()) {
            call.respond(
                HttpStatusCode.BadRequest,
                mapOf("error" to "Phone number is required")
            )
            return@get
        }

        try {
            val messages = messagesRepository.getLastMessages(phoneNumber)
            if (messages.isEmpty()) {
                call.respond(
                    HttpStatusCode.NotFound,
                    mapOf("error" to "No messages found for the provided phone number")
                )
            } else {
                call.respond(mapOf("messages" to messages))
            }
        } catch (e: Exception) {
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to "Failed to retrieve messages: ${e.message}")
            )
        }
    }

}