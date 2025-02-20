package com.example.messaging.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val sender:String,
    val message:String,
    val phoneNumber:String,
    val timestamp:Long?=null,
)
