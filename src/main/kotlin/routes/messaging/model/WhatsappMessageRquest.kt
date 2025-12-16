package com.example.routes.messaging.model

import kotlinx.serialization.Serializable

/**
 * This is used to send a message to whatsapp
 * */

@kotlinx.serialization.Serializable
data class WhatsAppMessageRequest(
    val messaging_product: String,
    val to: String,
    val type: String,
    val text: Text
)

@Serializable
data class Text(
    val body: String
)

@Serializable
data class WhatsappResponseBody(
    val error: WhatsappErrorMessage?=null,
)

@Serializable
data class WhatsappErrorMessage(
    val message:String,
    val error_data:List<ErrorData>?=null
    ){
    fun getErrorMessage():String{
        val errorData=StringBuilder()
        error_data?.forEach {
            errorData.append(it.details+" \n")
        }
        return "$message \n$errorData"
    }
}

@Serializable
data class ErrorData(
    val details:String?=null,
)