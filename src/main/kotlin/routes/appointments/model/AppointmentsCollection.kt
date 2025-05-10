package com.example.routes.appointments.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class AppointmentsCollection(
    @BsonId
    val id: ObjectId,
    val phoneNumber: String,
    val clientName: String,
    val date: String,
    val time: String,
    val status: String = "approved",
    val note: String? = null
){
    fun toModel():AppointmentModel{
        return AppointmentModel(
            id.toString(),
            phoneNumber,
            clientName,
            date,
            time,
            status,
            note,
        )
    }
}