package com.example.routes.users.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val id:Int?=null,
    val name:String,
    val phoneNumber:String,
    val email:String?=null,
    val note:String?=null,
    @Serializable val feedbackCollected:Boolean?=false,
)
