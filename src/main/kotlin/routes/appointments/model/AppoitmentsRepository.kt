package com.example.routes.appointments.model

import com.mongodb.client.model.Filters
import com.mongodb.client.model.Sorts
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.datetime.*

class AppointmentsRepository(
    private val mongoDatabase: MongoDatabase
) {

    private val collection=mongoDatabase.getCollection<AppointmentsCollection>("appointments")

    suspend fun getAll(
        onSuccess: suspend (List<AppointmentModel>)->Unit,
        onFailure: suspend (String)->Unit
    ){
        try {
            val result=collection.find()
                .sort(Sorts.descending(AppointmentsCollection::id.name))
                .limit(20)

            val appointments=result.map { it.toModel() }.toList()
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

            val result=collection.find()
                .filter(Filters.eq("status",status))
                .sort(Sorts.descending(AppointmentsCollection::id.name))
                .limit(20)

            val appointments=result.map { it.toModel() }.toList()
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

            val result=collection.find()
                .filter(Filters.eq("phoneNumber",phoneNumber))
                .sort(Sorts.descending(AppointmentsCollection::id.name))
                .limit(20)

            val appointments=result.map { it.toModel() }.toList()
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

            val result=collection.insertOne(appointmentModel.toCollection())
            if (result.insertedId!=null)
                onSuccess()
            else
                onFailure("Couldn't insert appointment into database")

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

            val filter=Filters.eq("id",appointmentModel.id)

            val updates=Updates.combine(
                Updates.set(AppointmentsCollection::clientName.name,appointmentModel.clientName),
                Updates.set(AppointmentsCollection::phoneNumber.name,appointmentModel.phoneNumber),
                Updates.set(AppointmentsCollection::note.name,appointmentModel.note),
                Updates.set(AppointmentsCollection::date.name,appointmentModel.date),
                Updates.set(AppointmentsCollection::time.name,appointmentModel.time),
                Updates.set(AppointmentsCollection::status.name,appointmentModel.status),
            )

            val result=collection.updateOne(filter,updates)

            if (result.modifiedCount>0)
                onSuccess()
            else
                onFailure("Could not update this appointment")

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
            val result=collection.deleteOne(Filters.eq("id",appointmentId))
            if (result.deletedCount>0)
                onSuccess()
            onFailure("Failed to delete appointment")
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