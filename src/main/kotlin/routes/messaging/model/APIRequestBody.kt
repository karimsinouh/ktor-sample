package com.example.routes.messaging.model

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

@Serializable
data class RequestBody(
    val model:String,
    val messages:List<AIMessage>,
    val response_format: ResponseFormat,
)

@Serializable
data class Parameters(
    val date:String?=null,
    val note:String?=null,
    val time:String?=null
)

@Serializable
data class StructuredResponseBody(
    val action:String,
    val parameters:Parameters?=null,
    val user_message:String,
)

@Serializable
data class ResponseBody(
    val error: APIError?=null,
    val choices:List<APIChoice>
)

@Serializable
data class APIChoice(
    val message: AIMessage,
)
@Serializable
data class APIError(
    val message:String,
)