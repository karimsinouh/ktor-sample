package com.example.routes.messaging.model

import com.example.routes.messaging.data.GenerateAIResponse
import kotlinx.serialization.Serializable

/**
 * This is used to send a message to whatsapp
 * */

@kotlinx.serialization.Serializable
data class WhatsAppMessageRequest(
    val messaging_product: String,
    val to: String,
    val type: String,
    val text: Text
)

@Serializable
data class Text(
    val body: String
)

@Serializable
data class WhatsappResponseBody(
    val error: WhatsappErrorMessage?=null,
)

@Serializable
data class WhatsappErrorMessage(
    val message:String,
    )