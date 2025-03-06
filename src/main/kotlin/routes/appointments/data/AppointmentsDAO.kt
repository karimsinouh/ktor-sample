package com.example.routes.appointments.data

import com.example.routes.appointments.model.AppointmentModel
import com.example.routes.appointments.model.AppointmentsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class AppointmentsDAO {

    fun insert(appointment: AppointmentModel)= transaction {

        AppointmentsTable.insert {
            it[clientName]=appointment.clientName
            it[phoneNumber]=appointment.phoneNumber
            it[date]=appointment.date
            it[time]=appointment.time
            it[status]=appointment.status
            it[note]=appointment.note
        }

    }

    fun getAll():List<AppointmentModel>{
        return transaction {

            AppointmentsTable.selectAll()
                .orderBy(AppointmentsTable.id to SortOrder.DESC)
                .limit(50)
                .map {
                    AppointmentModel.fromResultRow(it)
                }

        }
    }

    fun getAllByStatus(status:String):List<AppointmentModel>{
        return transaction {
            AppointmentsTable.selectAll()
                .where(AppointmentsTable.status eq status)
                .orderBy(AppointmentsTable.id to SortOrder.DESC)
                .map {
                    AppointmentModel.fromResultRow(it)
                }
        }
    }

    fun getAllByPhoneNumber(phoneNumber:String):List<AppointmentModel>{
        return transaction {
            AppointmentsTable.selectAll()
                .where(AppointmentsTable.status eq phoneNumber)
                .orderBy(AppointmentsTable.id to SortOrder.DESC)
                .map {
                    AppointmentModel.fromResultRow(it)
                }
        }
    }

    fun delete(id:Int){
        return transaction {
            AppointmentsTable.deleteWhere { AppointmentsTable.id eq id }
        }
    }

    fun update(appointment: AppointmentModel){
        return transaction {
            AppointmentsTable.update (where = {AppointmentsTable.id eq appointment.id}){
                it[note]=appointment.note
                it[date]=appointment.date
                it[time]=appointment.time
                it[status]=appointment.status
            }
        }
    }

}