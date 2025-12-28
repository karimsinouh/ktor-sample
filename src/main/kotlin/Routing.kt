package com.example

import com.example.core.SendNotification
import com.example.routes.messaging.presentation.messagesRouting
import com.example.di.DIModule
import com.example.features.config.presentation.configRouting
import com.example.features.errorsLog.presentation.errorsLogsRouting
import com.example.routes.templates.presentation.templatesRouting
import com.example.routes.users.model.UserModel
import features.users.presentation.usersRouting
import io.ktor.http.HttpStatusCode
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
        errorsLogsRouting(module.errorsLogs)

    }
}
