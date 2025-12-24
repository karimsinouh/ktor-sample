package com.example.features.errorsLog.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.features.errorsLog.data.ErrorsLogRepository
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
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

}