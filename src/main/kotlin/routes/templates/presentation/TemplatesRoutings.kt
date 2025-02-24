package com.example.routes.templates.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.templates.model.TemplatesRepository
import com.example.routes.templates.model.WhatsappTemplate
import com.example.routes.templates.model.WhatsappTemplateMessageRequest
import io.ktor.server.request.*
import io.ktor.server.routing.*


fun Routing.templatesRouting(
    templatesRepository: TemplatesRepository,
){

    post("templates/send") {

        try {
            val body=call.receive<WhatsappTemplateMessageRequest>()
            templatesRepository(
                template = body,
                onSuccess = { successResponse("Message Sent") },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Failed")
        }

    }

}