package com.example.routes.messaging.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.util.*

data class MessagesCollection(
    @BsonId
    val id:ObjectId,
    val sender:String,
    val message:String,
    val phoneNumber:String,
){
    fun toModel():MessageModel{
        return MessageModel(
            sender,message,phoneNumber
        )
    }
}
