package com.example.database.model

data class User(
    val id:Int,
    val name:String,
    val phoneNumber:String,
    val appointmentDate:String,
    val email:String?=null,
    val note:String?=null,
    val reminded:Boolean?=false,
    val feedbackCollected:Boolean?=false,
)
