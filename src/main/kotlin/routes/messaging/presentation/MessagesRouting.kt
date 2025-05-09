package com.example.routes.messaging.presentation

import com.example.routes.messaging.domain.ChatRepository
import io.ktor.server.routing.*

fun Routing.messagesRouting(chatRepository: ChatRepository) {
    getMessages(chatRepository.messages)
    messagesReceiver(chatRepository)
    verifyToken()
}