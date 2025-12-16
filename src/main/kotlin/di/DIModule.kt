package com.example.di

import com.example.routes.messaging.data.SendWhatsappMessage
import com.example.routes.messaging.domain.ChatRepository
import com.example.routes.messaging.data.MessagesRepository
import com.example.routes.messaging.domain.MessagesRepositoryImpl
import com.example.routes.templates.data.SendTemplateMessage
import com.example.routes.templates.domain.TemplatesRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import routes.users.domain.UsersRepositoryFirebaseImpl

class DIModule {

    private val client= HttpClient(CIO){
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation){
            json(
                Json {
                    ignoreUnknownKeys=true
                }
            )
        }
    }

    val messagesRepository by lazy {
        MessagesRepositoryImpl()
    }

    val sendWhatsappMessage by lazy {
        SendWhatsappMessage(client)
    }

    val chatRepository by lazy {
        ChatRepository(messagesRepository,sendWhatsappMessage)
    }

    val usersRepository by lazy {
        UsersRepositoryFirebaseImpl()
    }

    val sendTemplateMessage by lazy {
        SendTemplateMessage(client)
    }

    val templatesRepository by lazy {
        TemplatesRepository(sendTemplateMessage)
    }

}