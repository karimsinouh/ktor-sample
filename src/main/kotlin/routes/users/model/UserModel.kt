package com.example.routes.users.model

data class UserModel(
    val id:Int,
    val name:String,
    val phoneNumber:String,
    val email:String?=null,
    val note:String?=null,
    val feedbackCollected:Boolean?=false,
)
