package com.example.routes.messaging.data

import com.example.routes.messaging.model.MessageModel
import com.example.routes.messaging.model.MessagesCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.ascending
import com.mongodb.client.model.Sorts.descending
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

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