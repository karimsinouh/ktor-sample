package com.example.routes.users.model

import com.google.cloud.firestore.annotation.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    @DocumentId val id:String?=null,
    val name:String?=null,
    val phoneNumber:String?=null,
    val status: String?="pending",
    val age:String?=null,
    val pack: String?=null,
    val option:String?=null,
    val time:Long?=null,
    val comment:String?=null,
)