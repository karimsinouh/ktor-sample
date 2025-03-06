package com.example.routes.appointments.model

import com.example.routes.appointments.data.AppointmentsDAO
import kotlinx.datetime.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.postgresql.util.PSQLException


class AppointmentsRepository(private val dao:AppointmentsDAO) {

    suspend fun getAll(
        onSuccess: suspend (List<AppointmentModel>)->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {
            val appointments=dao.getAll()
            onSuccess(appointments)
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
            val appointments=dao.getAllByStatus(status)
            onSuccess(appointments)
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
            val appointments=dao.getAllByPhoneNumber(phoneNumber)
            onSuccess(appointments)
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
            dao.insert(appointmentModel)
            onSuccess()
        } catch (e:ExposedSQLException){

            // Check if the exception is due to a duplicate key violation
            if (e.cause is PSQLException && (e.cause as PSQLException).sqlState == "23505")
                onFailure("Duplicate appointment: An appointment already exists for this client, date, and time.")
            else
                // Handle other SQL exceptions
                onFailure("An error occurred while processing your request.")


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
            dao.update(appointmentModel)
            onSuccess()
        }catch (e:Exception){
            onFailure(e.message?:"Failed to update appointment")
        }
    }

    suspend fun delete(
        appointmentId: Int,
        onSuccess: suspend ()->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {
            dao.delete(appointmentId)
            onSuccess()
        }catch (e:Exception){
            onFailure(e.message?:"Failed to update appointment")
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