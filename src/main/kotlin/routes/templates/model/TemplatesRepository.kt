package com.example.routes.templates.model

import com.example.routes.templates.data.SendTemplateMessage
import io.ktor.client.*

class TemplatesRepository(
    private val sendTemplatesRepository: SendTemplateMessage
){

    suspend operator fun invoke(
        template: WhatsappTemplateMessageRequest,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit
    ){
        sendTemplatesRepository(template,onSuccess,onFailure)
    }

}