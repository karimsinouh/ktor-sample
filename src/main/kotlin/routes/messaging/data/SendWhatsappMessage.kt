package com.example.routes.messaging.data

import com.example.core.Env
import com.example.routes.messaging.model.Text
import com.example.routes.messaging.model.WhatsAppMessageRequest
import com.example.routes.messaging.model.WhatsappResponseBody
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*

class SendWhatsappMessage(
    private val client:HttpClient
) {

    suspend operator fun invoke(
        phoneNumber:String,
        message:String,
    ){

        val url="https://graph.facebook.com/v22.0/${Env.WHATSAPP_PHONE_NUMBER_ID}/messages"

        val response=client.post(url) {
            headers {
                append(HttpHeaders.ContentType,"application/json")
                append(HttpHeaders.Authorization,"Bearer ${Env.WHATSAPP_ACCESS_TOKEN}")
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

            if (responseBody.error!=null)
                throw IllegalArgumentException(responseBody.error.message)


        }else
            throw IllegalArgumentException(response.toString())

    }



}