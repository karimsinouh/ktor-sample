package com.example.features.errorsLog.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.features.errorsLog.data.ErrorsLogRepository
import com.example.features.errorsLog.model.ErrorLogModel
import io.ktor.server.request.receive
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlin.collections.emptyList

fun Routing.errorsLogsRouting(errorsLogsRepository: ErrorsLogRepository){

    get("/errorsLogs/get"){
        val errors=errorsLogsRepository.getLogs()

        if (errors.isSuccess){
            val list=errors.getOrNull() ?: emptyList()
            successResponse(list)
        }else{
            failureResponse("Couldn't load errors")
        }

    }

    post("/errorsLogs/update") {
        val body=call.receive<ErrorLogModel>()
        val response=errorsLogsRepository.update(body)
        if (response.isSuccess){
            successResponse("Error log has been successfully updated")
        }else{
            failureResponse(response.exceptionOrNull()?.message?:"Could not update error log")
        }
    }

}