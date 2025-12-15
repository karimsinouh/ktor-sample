package com.example.routes.appointments.model

import kotlinx.datetime.*

class AppointmentsRepository() {

    suspend fun getAll(
        onSuccess: suspend (List<AppointmentModel>)->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {

        }catch (e:Exception){
            onFailure(e.message?:"failed to retrieve appointments from database")
        }
    }

    suspend fun getByStatus(
        status:String,
        onSuccess: suspend (List<AppointmentModel>)->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {


        }catch (e:Exception){
            onFailure(e.message?:"failed to retrieve appointments from database")
        }
    }


    suspend fun getByPhoneNumber(
        phoneNumber:String,
        onSuccess: suspend (List<AppointmentModel>)->Unit,
        onFailure: suspend (String)->Unit
    ){

        try {


        }catch (e:Exception){
            onFailure(e.message?:"failed to retrieve appointments from database")
        }
    }

    suspend fun insert(
        appointmentModel: AppointmentModel,
        onSuccess: suspend ()->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {


        }catch (e:Exception){
            onFailure(e.message?:"Failed to insert appointment to database")
        }
    }

    suspend fun update(
        appointmentModel: AppointmentModel,
        onSuccess: suspend ()->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {

        }catch (e:Exception){
            onFailure(e.message?:"Failed to update appointment")
        }
    }

    suspend fun delete(
        appointmentId: String,
        onSuccess: suspend ()->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {

        }catch (e:Exception){
            onFailure(e.message?:"Failed to delete appointment")
        }
    }

    companion object{

        private const val THRESHOLD_24H= 24 * 60 * 60 * 1000L

        private fun getTimestamp(
            date:String,
            time:String,
        ):Long{

            val parsedDate= LocalDate.parse(date)
            val parsedTime= LocalTime.parse(time)

            val dateTime= LocalDateTime(parsedDate,parsedTime)

            val instant = dateTime.toInstant(TimeZone.currentSystemDefault())

            return instant.toEpochMilliseconds()

        }

        fun isAppointmentNear(
            date: String,
            time:String,
        ):Boolean{
            val currentMillis=System.currentTimeMillis()
            val appointmentMillis= getTimestamp(date,time)
            val difference=appointmentMillis-currentMillis
            return difference in 0..THRESHOLD_24H
        }
    }

}