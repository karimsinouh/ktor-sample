package com.example.routes.messaging.data

import com.example.routes.messaging.model.MessageModel

interface MessagesRepository {

    suspend fun insert(
        sender:String,
        message:String,
        userPhoneNumber:String,
    )

    suspend fun getLastMessages(
        userPhoneNumber: String,
    ):List<MessageModel>

}