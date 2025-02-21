package com.example.messaging.data

import com.example.routes.messaging.data.GenerateAIResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class SendWhatsappMessage(
    private val client:HttpClient
) {

    private val phoneNumberId="618261414699683"
    private val accessToken="EAAN3xaZBPt7ABO7ItAtWZAixCL9gp3AM63kmEyHFLsABVykojVMLCUO3xa4jMHr8TYaou37bAVs6j7oWZASTDepqieXa6dClO3vuwp8ZCBLQOCZAarnpGla3eCK9jZBLXdZBs8LowLQr5snBIUsSaCMYsMaP4KZAmB4pJvSt8YePB5MdbMKc2xldXtuYfqF4Nr0a0hpq5zZAroSO8qsLhP69T5etnUR78"

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
                val requestBody=WhatsAppMessageRequest(
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