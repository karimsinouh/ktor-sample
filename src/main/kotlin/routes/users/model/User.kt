package com.example.routes.users.model


import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id:ObjectId,
    val name:String,
    val phoneNumber:String,
    val status: String?="pending",
    val age:String,
    val pack: String,
    val option:String,
    val time:Long,
){

    fun toModel():UserModel{
        return UserModel(
            id.toString(),
            name,
            phoneNumber,
            status,
            age,
            pack,
            option,
            time,
        )
    }

}