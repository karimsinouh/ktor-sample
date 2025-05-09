package com.example.routes.messaging.data

import com.example.routes.messaging.model.MessageModel
import com.example.routes.messaging.model.MessagesCollection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts.descending
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.types.ObjectId

class MessagesRepository(
    private val mongoDatabase: MongoDatabase,
) {


    private val collection=mongoDatabase.getCollection<MessagesCollection>("messages")

    suspend fun insert(
        sender:String,
        message:String,
        userPhoneNumber:String,
    ){
        val obj=MessagesCollection(ObjectId(),sender,message, userPhoneNumber)
        collection.insertOne(obj)
    }

    suspend fun getLastMessages(
        userPhoneNumber: String,
    ):List<MessageModel>{
        val result=collection.find()
            .filter(Filters.eq("phoneNumber",userPhoneNumber))
            .sort(descending(MessagesCollection::id.name))
            .limit(10)
            .toList()

        return result.map { it.toModel() }
    }

}