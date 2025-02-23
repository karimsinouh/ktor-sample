package com.example.core.data

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(){

    val url = "jdbc:postgresql://dpg-cutmtcdsvqrc73e8t9f0-a.oregon-postgres.render.com:5432/appointments_whatsapp_ai_bot"
    val user = "appointments_whatsapp_ai_bot_user"
    val password = "1NnF9HaHdiDOA1NIE4HTQFA0gmD8O3Zj"

    Database.connect(
        url = url,
        driver = "org.postgresql.Driver", // Add the driver
        user = user, // Add the username
        password = password // Add the password
    )

}