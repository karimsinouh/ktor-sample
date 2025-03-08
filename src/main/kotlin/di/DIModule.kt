package com.example.di

import com.example.routes.appointments.data.AppointmentsDAO
import com.example.routes.appointments.model.AppointmentsRepository
import com.example.routes.messaging.data.GenerateAIResponse
import com.example.routes.messaging.data.MessagesDAO
import com.example.routes.messaging.data.SendWhatsappMessage
import com.example.routes.messaging.model.ChatRepository
import com.example.routes.messaging.model.MessagesRepository
import com.example.routes.templates.data.SendTemplateMessage
import com.example.routes.templates.model.TemplatesRepository
import com.example.routes.users.data.UsersDAO
import com.example.routes.users.model.UsersRepository
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


    private val messagesDao by lazy {
        MessagesDAO()
    }

    private val usersDao by lazy {
        UsersDAO()
    }

    private val appointmentsDao by lazy {
        AppointmentsDAO()
    }

    val generateAIResponse by lazy {
        GenerateAIResponse(client,usersRepository,appointmentsRepository)
    }

    val messagesRepository by lazy {
        MessagesRepository(messagesDao)
    }

    val sendWhatsappMessage by lazy {
        SendWhatsappMessage(client)
    }

    val chatRepository by lazy {
        ChatRepository(messagesRepository,generateAIResponse,sendWhatsappMessage)
    }

    val usersRepository by lazy {
        UsersRepository(usersDao)
    }

    val sendTemplateMessage by lazy {
        SendTemplateMessage(client)
    }

    val templatesRepository by lazy {
        TemplatesRepository(sendTemplateMessage)
    }

    val appointmentsRepository by lazy {
        AppointmentsRepository(appointmentsDao)
    }

}