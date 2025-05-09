package com.example.core.model

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

suspend fun RoutingContext.failureResponse(error:String){
    println("->ERROR->: $error ")
    call.respond(
        HttpStatusCode.InternalServerError,
       mapOf(
           "status" to "failure",
           "message" to error
       )
    )
}

suspend fun RoutingContext.successResponse(message:String){
    call.respond(
        HttpStatusCode.OK,
        mapOf(
            "status" to "success",
            "message" to message
        )
    )
}

suspend fun RoutingContext.successResponse(data:Any){
    call.respond(
        HttpStatusCode.OK,
        mapOf(
            "data" to data
        )
    )
}