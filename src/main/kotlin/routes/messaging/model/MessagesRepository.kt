package com.example.routes.messaging.model

import com.example.routes.messaging.data.MessagesDAO
import com.example.routes.messaging.model.MessageModel

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