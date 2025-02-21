package com.example.routes.messaging.model

import com.example.routes.messaging.data.GenerateAIResponse
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

fun List<MessageModel>.toAIMessages():List<GenerateAIResponse.AIMessage>{
    return map {
        val role=if (it.sender=="assistant") GenerateAIResponse.AIMessage.ROLE_ASSISTANT else GenerateAIResponse.AIMessage.ROLE_USER
        GenerateAIResponse.AIMessage(role = role, content = it.message)
    }
}