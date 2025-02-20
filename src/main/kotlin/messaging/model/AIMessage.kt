package com.example.messaging.model

import kotlinx.serialization.Serializable

@Serializable
data class AIMessage(
    val role:String,
    val content:String
){
    companion object{
        const val ROLE_USER="user"
        const val ROLE_ASSISTANT="assistant"
        const val ROLE_DEVELOPER="developer"
    }
}