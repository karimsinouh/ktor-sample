package com.example.di

import com.example.messaging.data.GenerateAIResponse
import com.example.messaging.data.MessagesDAO
import com.example.messaging.data.SendWhatsappMessage
import com.example.messaging.model.ChatRepository
import com.example.messaging.model.MessagesRepository
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.contentnegotiation.*
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

    val generateAIResponse by lazy {
        GenerateAIResponse(client)
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

}