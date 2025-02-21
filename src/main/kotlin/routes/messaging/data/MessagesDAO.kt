package com.example.routes.messaging.data

import com.example.routes.messaging.model.MessageModel
import com.example.routes.messaging.model.MessageTable
import com.example.routes.messaging.model.MessageTable.phoneNumber
import com.example.routes.messaging.model.MessageTable.timestamp
import com.example.routes.messaging.model.toMessage
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class MessagesDAO {

    fun insert(message: MessageModel):Int{
        return transaction {
            MessageTable.insert {
                it[sender] = message.sender
                it[MessageTable.message] = message.message
                it[timestamp] = message.timestamp?:0
                it[phoneNumber] = message.phoneNumber
            }[MessageTable.id]
        }
    }

    fun getMessagesFromPhoneNumber(
        userPhoneNumber:String,
    ):List<MessageModel>{
        return transaction {
            MessageTable.selectAll()
                .where(phoneNumber eq userPhoneNumber)
                .orderBy(timestamp to SortOrder.DESC)
                .limit(10)
                .map {
                    it.toMessage()
                }.reversed()
        }
    }

}