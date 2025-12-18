package com.example.features.messaging.model

import kotlinx.serialization.Serializable

@Serializable
data class MessageModel(
    val sender:String?=null,
    val message:String?=null,
    val phoneNumber:String?=null,
    val timestamp:Long?=null,
)