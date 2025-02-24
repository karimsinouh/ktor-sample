package com.example.core.data

import com.example.core.Constants
import io.ktor.server.application.*
import org.jetbrains.exposed.sql.Database

fun Application.configureDatabase(){

    Database.connect(
        url = Constants.DATABASE_URL,
        driver = "org.postgresql.Driver", // Add the driver
        user = Constants.DATABASE_USER, // Add the username
        password = Constants.DATABASE_PASSWORD // Add the password
    )

}