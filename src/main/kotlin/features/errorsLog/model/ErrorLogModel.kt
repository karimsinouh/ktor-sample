package com.example.features.errorsLog.model

import com.google.cloud.firestore.annotation.DocumentId
import kotlinx.serialization.Serializable

@Serializable
data class ErrorLogModel(
    @DocumentId val id: String? = null,
    val level: String = "ERROR",
    val message: String = "",
    val stackTrace: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val path: String = "unknown",
    val seen:Boolean?=false
)