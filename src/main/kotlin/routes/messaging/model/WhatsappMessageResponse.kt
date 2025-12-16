package com.example.routes.messaging.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * This is received from Meta via webhook
 * */

@Serializable
data class WhatsAppMessageResponse(
    @SerialName("object") val objectType: String,
    val entry: List<Entry>
)

@Serializable
data class Entry(
    val id: String,
    val changes: List<Change>
)
@Serializable
data class Change(
    val value: Value,
    val field: String
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
    val text: Text,
    val type: String
)