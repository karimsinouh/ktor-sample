package com.example.routes.messaging.model

import kotlinx.serialization.Serializable


@Serializable
data class WhatsAppBusinessAccount(
    val `object`: String,
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

@Serializable
data class Text(
    val body: String
)


//@Serializable
//data class WhatsappMessage(
//    val field: String,
//    val value: Value
//)
//
//@Serializable
//data class Value(
//    val messaging_product: String,
//    val metadata: Metadata,
//    val contacts: List<Contact>,
//    val messages: List<Message>
//)
//
//@Serializable
//data class Metadata(
//    val display_phone_number: String,
//    val phone_number_id: String
//)
//
//@Serializable
//data class Contact(
//    val profile: Profile,
//    val wa_id: String
//)
//
//@Serializable
//data class Profile(
//    val name: String
//)
//
//@Serializable
//data class Message(
//    val from: String,
//    val id: String,
//    val timestamp: String,
//    val type: String,
//    val text: Text
//)
//
//@Serializable
//data class Text(
//    val body: String
//)