package com.example.messaging.presentation

import com.example.messaging.model.ChatRepository
import com.example.messaging.model.MessagesRepository
import io.ktor.server.routing.*

fun Routing.messagesRouting(chatRepository: ChatRepository) {
    getMessages(chatRepository.messages)
    messagesReceiver(chatRepository)
}