package com.example.features.config.data

import com.example.features.config.domain.ConfigRepository
import com.example.features.config.model.ConfigModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GlobalConfigsHolder(private val configRepository: ConfigRepository) {


    var configs: ConfigModel?=null

    suspend fun load(){
        val result=configRepository.getConfigs()

        if (result.isSuccess)
            configs=result.getOrNull()
        else{
            //log this error

        }
    }

    fun updateInMemory(newConfigs: ConfigModel){
        configs=newConfigs
    }

    fun updateInMemory(apiKey: String){
        configs=configs?.copy(geminiKey =apiKey)
    }

}