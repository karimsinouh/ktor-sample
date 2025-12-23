package com.example.features.config.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.features.config.data.GlobalConfigsHolder
import com.example.features.config.domain.ConfigRepository
import com.example.features.config.model.ConfigModel
import io.ktor.server.request.receive
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post

fun Routing.configRouting(
    configsRepository: ConfigRepository,
    globalConfigsHolder: GlobalConfigsHolder
){

    post("/configs/update"){

        val requestBody=call.receive<ConfigModel>()

        configsRepository.updateConfigs(requestBody)
        globalConfigsHolder.updateInMemory(requestBody)
        successResponse("Configs updated!")

    }

    post("/configs/apikey/update") {
        val apiKey=call.parameters["key"]
        configsRepository.updateApiKey(apiKey)
        successResponse("API Key updated!")
    }

    get("/configs/get"){

        val result=configsRepository.getConfigs()

        if (result.isSuccess){
            val configs=result.getOrNull()

            if (configs==null){
                failureResponse("configs are not set yet")
            }else{
                val modifiedConfigs=configs.copy(geminiKey = null)
                successResponse(modifiedConfigs)
            }

        }else
            failureResponse(result.exceptionOrNull()?.message?:"Something went wrong while trying to fetch configs")

    }



}