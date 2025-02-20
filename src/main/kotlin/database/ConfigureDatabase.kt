package com.example.database

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(){
    Database.connect(
        url="jdbc:postgresql://localhost:5432/postgres",
        user = "postgres",
        password = "karims2002"
    )
}