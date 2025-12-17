package com.example.routes.messaging.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class MessageModel(
    val sender:String?=null,
    val message:String?=null,
    val phoneNumber:String?=null,
    val timestamp:Long?=null,
)