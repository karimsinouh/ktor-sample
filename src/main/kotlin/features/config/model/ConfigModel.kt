package com.example.features.config.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigModel(
    val trainingPrompt:String?=null,
    val messagesLimit: Int?=15,
    val geminiKey: String?=null,
    val maintenanceMode: Boolean?=false,
)