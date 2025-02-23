package com.example.core.data

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(){

    Database.connect(
        url="jdbc:postgresql://appointments_whatsapp_ai_bot_user:1NnF9HaHdiDOA1NIE4HTQFA0gmD8O3Zj@dpg-cutmtcdsvqrc73e8t9f0-a.oregon-postgres.render.com:5432/appointments_whatsapp_ai_bot",
        user = "appointments_whatsapp_ai_bot_user",
        password = "1NnF9HaHdiDOA1NIE4HTQFA0gmD8O3Zj",
        driver = "org.postgresql.Driver"
    )
}