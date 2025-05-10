package com.example.routes.messaging.model


data class MessagesCollection(
    val sender:String,
    val message:String,
    val phoneNumber:String,
    val timestamp:Long,
){
    fun toModel():MessageModel{
        return MessageModel(
            sender,message,phoneNumber,timestamp,
        )
    }
}
