package com.example.routes.users.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class UserModel(
    val id:String?=null,
    val name:String,
    val phoneNumber:String,
    val email:String?=null,
    val note:String?="",
    val status: String?="pending",
){
    fun toRequest():User{
        return User(
            ObjectId(),
            name,
            phoneNumber,
            email,
            note,
            status
        )
    }
}