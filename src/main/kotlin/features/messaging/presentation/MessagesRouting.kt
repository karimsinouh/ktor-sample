package com.example.routes.messaging.presentation

import com.example.di.DIModule
import com.example.features.messaging.presentation.messagesReceiver
import com.example.features.messaging.presentation.verifyTokenKoog
import io.ktor.server.routing.*

fun Routing.messagesRouting(module: DIModule) {
    getMessages(module.chatRepository.messages)
    messagesReceiver(module)
    verifyTokenKoog()
}