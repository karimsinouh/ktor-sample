package com.example.routes.messaging.model

import com.example.routes.messaging.data.GenerateAIResponse
import com.example.messaging.data.SendWhatsappMessage

data class ChatRepository(
    val messages: MessagesRepository,
    val generateAIResponse: GenerateAIResponse,
    val sendWhatsappMessage: SendWhatsappMessage,
)
