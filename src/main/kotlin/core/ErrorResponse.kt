package com.example.core

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun RoutingContext.errorResponse(error:String){
    call.respond(
        HttpStatusCode.InternalServerError,
        mapOf("error" to error)
    )
}

suspend fun RoutingContext.successResponse(message:String){
    call.respond(
        HttpStatusCode.OK,
        mapOf(
            "success" to true,
            "message" to message
        )
    )
}

suspend fun RoutingContext.successResponse(result:Any){
    call.respond(
        HttpStatusCode.OK,
        mapOf("result" to result)
    )
}