package com.example.routes.messaging.domain

import com.example.routes.messaging.data.SendWhatsappMessage
import com.example.routes.messaging.data.MessagesRepository

data class ChatRepository(
    val messages: MessagesRepository,
    val sendWhatsappMessage: SendWhatsappMessage,
)
