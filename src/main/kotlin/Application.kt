package com.example

 import ai.koog.agents.core.tools.reflect.tools
 import ai.koog.ktor.Koog
 import com.example.di.DIModule
 import com.example.routes.users.domain.UsersToolSet
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
    install(Koog){
        llm {
            google(apiKey = "AIzaSyBgBrwtSjRoqS8Vsnwn5tzi-H6hyrnp6E0")
        }
        agentConfig {
            registerTools {
                val usersTools=UsersToolSet(dependencyInjectionModule.usersRepository)
                tools(usersTools)
            }
        }
    }


    configureRouting(dependencyInjectionModule)

}
