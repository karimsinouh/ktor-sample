package com.example.routes.messaging.model

import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class MessageModel(
    val sender:String,
    val message:String,
    val phoneNumber:String,
    val timestamp:Long,
){
    fun toAiMessage():AIMessage{
        return AIMessage(sender,message)
    }
}