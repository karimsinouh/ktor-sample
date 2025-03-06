package com.example.routes.appointments.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.appointments.model.AppointmentModel
import com.example.routes.appointments.model.AppointmentsRepository
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

fun Routing.appointmentsRouting(repo: AppointmentsRepository){

    get("appointments/testTime") {
        try {
            val date=LocalDate(2025,3,7).toString()
            val time=LocalTime(8,45).toString()


            val isNear=AppointmentsRepository.isAppointmentNear(date,time)


            call.respond(
                HttpStatusCode.OK,
                if (isNear) "near" else "not near"
            )
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }

    }

    get("appointments/all"){
        try {
            repo.getAll(
                onSuccess = ::successResponse,
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Server Failed")
        }
    }

    put("appointments/new"){
        try{
            val appointment=call.receive<AppointmentModel>()
            repo.insert(
                appointmentModel = appointment,
                onSuccess = { successResponse("Appointment Inserted") },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Server Failed")
        }
    }

    post("appointments/update") {
        try{
            val appointment=call.receive<AppointmentModel>()
            repo.update(
                appointmentModel = appointment,
                onSuccess = { successResponse("Appointment Updated") },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Server Failed")
        }
    }

    delete("appointments/delete"){
        try {
            val appointmentModel=call.receive<AppointmentModel>()
            repo.delete(
                appointmentId = appointmentModel.id,
                onSuccess = {successResponse("Deleted")},
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"Server Failed.")
        }
    }

}