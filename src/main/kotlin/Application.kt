package com.example

 import com.example.di.DIModule
 import com.example.core.FirebaseAdmin
 import io.ktor.http.HttpStatusCode
 import io.ktor.serialization.kotlinx.json.json
 import io.ktor.server.application.*
 import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
 import io.ktor.server.plugins.statuspages.StatusPages
 import io.ktor.server.resources.Resources
 import io.ktor.server.response.respond
 import kotlinx.coroutines.runBlocking
 import kotlinx.serialization.json.Json

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    FirebaseAdmin.init()
    val dependencyInjectionModule=DIModule()
    runBlocking {
        dependencyInjectionModule.globalConfigsHolder.load()
    }

    install(Resources)
    install(ContentNegotiation){
        json(Json {
            ignoreUnknownKeys = true
        })
    }


    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = mapOf(
                    "message" to (cause.message ?: "Unknown error")
                )
            )
            // Log the error here instead of in every route
            call.application.environment.log.error("Global Error", cause)
        }
    }

    configureRouting(dependencyInjectionModule)

}
