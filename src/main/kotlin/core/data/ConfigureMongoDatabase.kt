package com.example.core.data

import com.example.core.Constants
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase

fun getMongoDatabase():MongoDatabase{
    System.setProperty("java.naming.provider.url", "dns://8.8.8.8")
    return MongoClient.create(Constants.MONGO_DATABASE_CREDENTIALS)
        .getDatabase(Constants.MONGO_DATABASE_NAME)
}