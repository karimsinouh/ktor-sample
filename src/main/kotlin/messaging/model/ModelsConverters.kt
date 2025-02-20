package com.example.messaging.model

import com.example.chat.model.MessageTable
import org.jetbrains.exposed.sql.ResultRow

fun ResultRow.toMessage(): MessageModel {
    val message=this
    return MessageModel(
        sender = message[MessageTable.sender],
        message = message[MessageTable.message],
        timestamp = message[MessageTable.timestamp],
        phoneNumber = message[MessageTable.phoneNumber]
    )
}

fun List<MessageModel>.toAIMessages():List<AIMessage>{
    return map {
        val role=if (it.sender=="assistant") AIMessage.ROLE_ASSISTANT else AIMessage.ROLE_USER
        AIMessage(role=role, content = it.message)
    }
}