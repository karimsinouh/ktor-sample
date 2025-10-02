package com.example.routes.users.model

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id:ObjectId,
    val name:String,
    val phoneNumber:String,
    val email:String?=null,
    val note:String?="",
    val status: String?="pending",
){

    fun toModel():UserModel{
        return UserModel(
            id.toString(),
            name,
            phoneNumber,
            email,
            note,
            status
        )
    }

}