package com.example

 import com.example.di.DIModule
 import com.example.core.FirebaseAdmin
 import com.example.features.errorsLog.model.ErrorLogModel
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


            //log it in db as well
            val logEntry = ErrorLogModel(
                level = "ERROR",
                message = cause.message ?: "Unknown",
                stackTrace = cause.stackTraceToString().take(1000), // Limit size to save DB space
                path = call.request.local.uri
            )
            val logRequest=dependencyInjectionModule.errorsLogs.log(logEntry)
            logRequest.onSuccess {

            }.onFailure {
                print(it.message?:"couldn't log error in db")
            }
        }
    }

    configureRouting(dependencyInjectionModule)

}
