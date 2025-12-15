package com.example

 import com.example.di.DIModule
 import di.FirebaseAdmin
 import io.ktor.serialization.kotlinx.json.json
 import io.ktor.server.application.*
 import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
 import io.ktor.server.resources.Resources
 import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    FirebaseAdmin.init()
    val dependencyInjectionModule=DIModule()

    install(Resources)
    install(ContentNegotiation){
        json(Json {
            ignoreUnknownKeys = true
        })
    }

    configureRouting(dependencyInjectionModule)

}
