package com.example.routes.appointments.model

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class AppointmentModel(
    val id: Int,
    val phoneNumber: String,
    val clientName: String,
    val date: String,
    val time: String,
    val status: String = "approved",
    val note: String? = null
){
    companion object{
        fun fromResultRow(result:ResultRow):AppointmentModel{
            return AppointmentModel(
                result[AppointmentsTable.id],
                result[AppointmentsTable.phoneNumber],
                result[AppointmentsTable.clientName],
                result[AppointmentsTable.date],
                result[AppointmentsTable.time],
                result[AppointmentsTable.status],
                result[AppointmentsTable.note],
            )
        }
    }
}