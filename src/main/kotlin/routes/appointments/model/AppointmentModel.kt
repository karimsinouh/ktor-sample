package com.example.routes.appointments.model

import kotlinx.serialization.Serializable
import org.bson.types.ObjectId
import org.jetbrains.exposed.sql.ResultRow

@Serializable
data class AppointmentModel(
    val id: String,
    val phoneNumber: String,
    val clientName: String,
    val date: String,
    val time: String,
    val status: String = "approved",
    val note: String? = null
){

    fun toCollection():AppointmentsCollection{
        return AppointmentsCollection(
            ObjectId(),
            phoneNumber,
            clientName,
            date,
            time,
            status,
            note,
        )
    }

    companion object{

        fun listToText(list:List<AppointmentModel>):String{
            val strBuilder=StringBuilder()

            if (list.isEmpty()){
                return "You don't have any appointments yet"
            }

            list.forEachIndexed { i, it ->
                strBuilder.append("""
                   -Appointment #${i+1}
                   
                       Date: *${it.date}* 
                       
                       Time: *${it.time}*
                       
                       Note: *${it.note}*
                       
                       Status: *${it.status}*
                    
                    
                """.trimIndent())
            }

            return strBuilder.toString()
        }

    }

}