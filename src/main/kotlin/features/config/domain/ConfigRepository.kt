package com.example.features.config.domain

import com.example.features.config.model.ConfigModel

interface ConfigRepository {

    suspend fun updateConfigs(configs: ConfigModel)
    suspend fun updateApiKey(newKey: String?)
    suspend fun getConfigs(): Result<ConfigModel>

}