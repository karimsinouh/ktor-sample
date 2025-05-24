package com.example.routes.messaging.model

import com.example.core.ConfigureAIModel
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
data class ResponseBody(
    val error: APIError?=null,
    val choices:List<APIChoice>?=null
)


@Serializable
data class StructuredResponseBody(
    val action:String?=null,
    val parameters:ConfigureAIModel.Properties.Parameters?=null,
    val user_message:String?=null,
)

@Serializable
data class APIChoice(
    val message: AIMessage,
)
@Serializable
data class APIError(
    val message:String,
)