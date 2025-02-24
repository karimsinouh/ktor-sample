package com.example.routes.messaging.model

import kotlinx.serialization.Serializable

@Serializable
data class WhatsappMessage(
    val field: String,
    val value: Value
)

@Serializable
data class Value(
    val messaging_product: String,
    val metadata: Metadata,
    val contacts: List<Contact>,
    val messages: List<Message>
)

@Serializable
data class Metadata(
    val display_phone_number: String,
    val phone_number_id: String
)

@Serializable
data class Contact(
    val profile: Profile,
    val wa_id: String
)

@Serializable
data class Profile(
    val name: String
)

@Serializable
data class Message(
    val from: String,
    val id: String,
    val timestamp: String,
    val type: String,
    val text: Text
)

@Serializable
data class Text(
    val body: String
)