package com.example.messaging.model

import com.example.messaging.data.GenerateAIResponse
import com.example.messaging.data.SendWhatsappMessage

data class ChatRepository(
    val messages:MessagesRepository,
    val generateAIResponse: GenerateAIResponse,
    val sendWhatsappMessage: SendWhatsappMessage,
)
