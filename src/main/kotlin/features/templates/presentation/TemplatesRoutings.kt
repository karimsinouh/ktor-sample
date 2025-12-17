package com.example.routes.templates.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.templates.domain.TemplatesRepository
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
                onSuccess = { successResponse(it) },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Failed")
        }

    }

}