package com.example.routes.messaging.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.messaging.data.MessagesRepository
import io.ktor.server.routing.*

fun Routing.getMessages(messagesRepository: MessagesRepository){

    // Endpoint to get messages by phone number
    get("messages/get/{phoneNumber}") {

        val phoneNumber = call.parameters["phoneNumber"]

        println("Got msgs for $phoneNumber")

        if (phoneNumber.isNullOrBlank()) {
            failureResponse("Phone number is required")
            return@get
        }

        try {
            val messages = messagesRepository.getLastMessages(phoneNumber)
            successResponse(messages)
        } catch (e: Exception) {
            failureResponse("Failed to retrieve messages: ${e.message}")
        }
    }



}