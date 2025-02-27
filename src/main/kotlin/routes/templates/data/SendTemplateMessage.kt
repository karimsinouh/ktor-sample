package com.example.routes.templates.data

import com.example.core.Constants
import com.example.routes.messaging.model.WhatsappResponseBody
import com.example.routes.templates.model.TemplateComponentParameter
import com.example.routes.templates.model.WhatsappTemplate
import com.example.routes.templates.model.WhatsappTemplateMessageRequest
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.intellij.lang.annotations.Language

class SendTemplateMessage(
    private val client: HttpClient
) {

    suspend operator fun invoke(
        template: WhatsappTemplateMessageRequest,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit
    ){
        try {

            val response=client.post("https://graph.facebook.com/v22.0/${Constants.WHATSAPP_PHONE_NUMBER_ID}/messages"){
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                    append(HttpHeaders.Authorization,"Bearer ${Constants.WHATSAPP_ACCESS_TOKEN}")
                }
                setBody(template)
            }

            if (response.status.value==200)
                onSuccess(response.bodyAsText())
            else{
                val error=response.body<WhatsappResponseBody>()
                onFailure(error.error?.getErrorMessage()?:"API Failure")
            }


        }catch (e:Exception){
            onFailure(e.message?:"Failed to send template message")
        }
    }

}