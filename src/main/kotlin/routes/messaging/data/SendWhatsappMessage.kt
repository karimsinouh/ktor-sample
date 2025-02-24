package com.example.routes.messaging.data

import com.example.core.Constants
import com.example.routes.messaging.model.Text
import com.example.routes.messaging.model.WhatsAppMessageRequest
import com.example.routes.messaging.model.WhatsappResponseBody
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class SendWhatsappMessage(
    private val client:HttpClient
) {

    suspend operator fun invoke(
        phoneNumber:String,
        message:String,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit
    ){

        val url="https://graph.facebook.com/v22.0/${Constants.WHATSAPP_PHONE_NUMBER_ID}/messages"
        try {

            val response=client.post(url) {
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                    append(HttpHeaders.Authorization,"Bearer ${Constants.WHATSAPP_ACCESS_TOKEN}")
                }
                val requestBody= WhatsAppMessageRequest(
                    messaging_product = "whatsapp",
                    to = phoneNumber,
                    type = "text",
                    text = Text(message)
                )
                setBody(requestBody)
            }

            if (response.status.value==200){
                val responseBody: WhatsappResponseBody =response.body()

                if (responseBody.error!=null){
                    onFailure(responseBody.error.message)
                }else{
                    onSuccess("Message ($message) sent to $phoneNumber")
                }

            }else{
                onFailure(response.toString())
            }

        }catch (e:Exception){
            onFailure(e.message?:"Failed to send WhatApp message")
        }

    }



}