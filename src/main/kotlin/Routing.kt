package com.example

import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.ktor.Koog
import com.example.routes.messaging.presentation.messagesRouting
import com.example.di.DIModule
import com.example.routes.appointments.presentation.appointmentsRouting
import com.example.routes.templates.presentation.templatesRouting
import com.example.routes.users.domain.UsersToolSet
import com.example.routes.users.presentation.usersRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun Application.configureRouting(module:DIModule) {

    install(Resources)
    install(ContentNegotiation){
        json(Json {
            ignoreUnknownKeys = true
        })
    }
    install(Koog){
        llm {
            google(apiKey = "AIzaSyBgBrwtSjRoqS8Vsnwn5tzi-H6hyrnp6E0")
        }
        agentConfig {
            registerTools {
                val usersTools=UsersToolSet(module.usersRepository)
                tools(usersTools)
            }
        }
    }

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        messagesRouting(module.chatRepository,module.usersRepository)
        usersRouting(module.usersRepository)

        templatesRouting(module.templatesRepository)

        appointmentsRouting(module.appointmentsRepository)
    }
}
