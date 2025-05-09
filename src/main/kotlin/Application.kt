package com.example

import com.example.core.data.configureDatabase
import com.example.di.DIModule
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    val dependencyInjectionModule=DIModule()
    configureRouting(dependencyInjectionModule)
    //configureDatabase()

}
