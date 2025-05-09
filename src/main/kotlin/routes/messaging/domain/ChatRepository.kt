package com.example.routes.messaging.domain

import com.example.routes.messaging.data.GenerateAIResponse
import com.example.routes.messaging.data.SendWhatsappMessage
import com.example.routes.messaging.data.MessagesRepository

data class ChatRepository(
    val messages: MessagesRepository,
    val generateAIResponse: GenerateAIResponse,
    val sendWhatsappMessage: SendWhatsappMessage,
)
