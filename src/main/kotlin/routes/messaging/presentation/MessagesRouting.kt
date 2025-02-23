package com.example.routes.messaging.presentation

import com.example.routes.messaging.model.ChatRepository
import com.example.routes.messaging.presentation.getMessages
import com.example.routes.messaging.presentation.messagesReceiver
import io.ktor.server.routing.*

fun Routing.messagesRouting(chatRepository: ChatRepository) {
    getMessages(chatRepository.messages)
    messagesReceiver(chatRepository)
    verifyToken()
}