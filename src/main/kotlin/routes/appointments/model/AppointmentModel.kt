package com.example.routes.appointments.model

import kotlinx.serialization.Serializable

@Serializable
data class AppointmentModel(
    val id: String,
    val phoneNumber: String,
    val clientName: String,
    val date: String,
    val time: String,
    val status: String = "approved",
    val note: String? = null
)