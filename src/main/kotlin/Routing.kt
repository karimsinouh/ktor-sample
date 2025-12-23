package com.example

import com.example.routes.messaging.presentation.messagesRouting
import com.example.di.DIModule
import com.example.features.config.presentation.configRouting
import com.example.routes.templates.presentation.templatesRouting
import features.users.presentation.usersRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(module:DIModule) {

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        messagesRouting(module)
        usersRouting(module.usersRepository)
        templatesRouting(module.templatesRepository)
        configRouting(module.configsRepository,module.globalConfigsHolder)

    }
}
