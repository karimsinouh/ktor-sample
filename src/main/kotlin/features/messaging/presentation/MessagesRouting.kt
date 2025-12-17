package com.example.routes.messaging.presentation

import com.example.di.DIModule
import features.messaging.data.ChatRepository
import features.users.domain.UsersRepository
import io.ktor.server.routing.*

fun Routing.messagesRouting(module: DIModule) {
    getMessages(module.chatRepository.messages)
    messagesReceiver(module.processIncomingWhatsappMessages)
    verifyTokenKoog()
}