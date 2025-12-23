package com.example.routes.messaging.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import features.messaging.domain.MessagesRepository
import io.ktor.server.routing.*

fun Routing.getMessages(messagesRepository: MessagesRepository){

    // Endpoint to get messages by phone number
    get("messages/get/{phoneNumber}") {

        val phoneNumber = call.parameters["phoneNumber"]

        if (phoneNumber.isNullOrBlank()) {
            failureResponse("Phone number is required")
            return@get
        }

        val messages = messagesRepository.getLastMessages(phoneNumber,20)
        successResponse(messages)
    }



}