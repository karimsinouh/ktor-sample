package com.example.messaging.model

import com.example.messaging.data.MessagesDAO

class MessagesRepository(private val dao: MessagesDAO) {

    fun insert(
        sender:String,
        userPhoneNumber:String,
        message:String,
    ){
        val timestamp=System.currentTimeMillis()
        dao.insert(MessageModel(sender,message,userPhoneNumber,timestamp))
    }

    fun getLastMessages(
        userPhoneNumber: String,
    ):List<MessageModel>{
        return dao.getMessagesFromPhoneNumber(userPhoneNumber)
    }

}