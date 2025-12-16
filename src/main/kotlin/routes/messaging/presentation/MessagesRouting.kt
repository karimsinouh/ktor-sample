package com.example.routes.messaging.presentation

import com.example.routes.messaging.domain.ChatRepository
import com.example.routes.users.data.UsersRepository
import io.ktor.server.routing.*

fun Routing.messagesRouting(chatRepository: ChatRepository,usersRepository: UsersRepository) {
    getMessages(chatRepository.messages)
    messagesReceiverKoog(chatRepository,usersRepository)
    verifyTokenKoog()
}