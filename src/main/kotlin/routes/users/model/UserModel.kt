package com.example.routes.users.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class UserModel(
    val id:String?=null,
    val name:String,
    val phoneNumber:String,
    val status: String?="pending",
    val age:String,
    val pack: String,
    val option:String,
    val time:Long,
){
    fun toRequest():User{
        return User(
            ObjectId(),
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