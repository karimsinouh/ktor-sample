package com.example.di

import com.example.core.AgentCore
import com.example.features.config.data.ConfigRepositoryImpl
import com.example.features.config.data.GlobalConfigsHolder
import com.example.features.config.domain.ConfigRepository
import com.example.features.errorsLog.data.ErrorsLogRepository
import com.example.routes.messaging.data.SendWhatsappMessageImpl
import features.messaging.data.ChatRepository
import features.messaging.data.MessagesRepositoryImpl
import com.example.routes.templates.data.SendTemplateMessage
import com.example.routes.templates.domain.TemplatesRepository
import features.messaging.useCase.ProcessIncomingWhatsappMessages
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import features.users.data.UsersRepositoryFirebaseImpl

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
        SendWhatsappMessageImpl(client)
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

    val agent by lazy {
        AgentCore(usersRepository,globalConfigsHolder)
    }

    val processIncomingWhatsappMessages by lazy {
        ProcessIncomingWhatsappMessages(chatRepository,agent)
    }

    val configsRepository by lazy {
        ConfigRepositoryImpl()
    }

    val globalConfigsHolder= GlobalConfigsHolder(configsRepository)

    val errorsLogs by lazy {
        ErrorsLogRepository()
    }

}