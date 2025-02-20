package com.example

import com.example.messaging.presentation.messagesRouting
import com.example.di.DIModule
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(module:DIModule) {

    install(Resources)
    install(ContentNegotiation){
        json()
    }

    routing {

        get("/") {
            call.respondText("Hello World!")
        }

        messagesRouting(module.chatRepository)
    }
}
