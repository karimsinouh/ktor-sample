package com.example.di

import com.example.core.data.getMongoDatabase
import com.example.routes.appointments.model.AppointmentsRepository
import com.example.routes.messaging.data.GenerateAIResponse
import com.example.routes.messaging.data.SendWhatsappMessage
import com.example.routes.messaging.domain.ChatRepository
import com.example.routes.messaging.data.MessagesRepository
import com.example.routes.templates.data.SendTemplateMessage
import com.example.routes.templates.domain.TemplatesRepository
import com.example.routes.users.domain.UsersRepositoryImpl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

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

    val mongoDatabase by lazy {
        getMongoDatabase()
    }


    val generateAIResponse by lazy {
        GenerateAIResponse(client,usersRepository)
    }

    val messagesRepository by lazy {
        MessagesRepository(mongoDatabase)
    }

    val sendWhatsappMessage by lazy {
        SendWhatsappMessage(client)
    }

    val chatRepository by lazy {
        ChatRepository(messagesRepository,generateAIResponse,sendWhatsappMessage)
    }

    val usersRepository by lazy {
        UsersRepositoryImpl(mongoDatabase)
    }

    val sendTemplateMessage by lazy {
        SendTemplateMessage(client)
    }

    val templatesRepository by lazy {
        TemplatesRepository(sendTemplateMessage)
    }

    val appointmentsRepository by lazy {
        AppointmentsRepository(mongoDatabase)
    }

}