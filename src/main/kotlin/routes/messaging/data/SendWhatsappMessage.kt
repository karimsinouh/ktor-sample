package com.example.routes.messaging.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class SendWhatsappMessage(
    private val client:HttpClient
) {

    private val phoneNumberId="595557563638834"
    private val accessToken="EAAN3xaZBPt7ABOwd55Hy8hNzlO1MnYGrRZBHrqcRkdC7bvjvEN19OTJ1PZB5JvU7aBgWZAFU6CtZBugbEh2w12K4nJjZB0xM5oXkdPUU4DNwqrRelinnw6HmKGZCkOlHdJUrJrA7edGZBZAgmKLVqJrX7O5tMdrz48vLemDVJsZBX58sb2kWZC1YzozyAd0733Gbks1TpZC6NJdGoMM0jdq49q5k0w2tZA6JXmlfuDym5oTu2"

    suspend operator fun invoke(
        phoneNumber:String,
        message:String,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit
    ){

        val url="https://graph.facebook.com/v22.0/$phoneNumberId/messages"
        try {

            val response=client.post(url) {
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                    append(HttpHeaders.Authorization,"Bearer $accessToken")
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
        val error: GenerateAIResponse.APIError?=null,
    )
}