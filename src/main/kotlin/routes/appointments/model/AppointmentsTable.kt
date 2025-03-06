package com.example.routes.appointments.model


import org.jetbrains.exposed.sql.Table

object AppointmentsTable:Table("appointments") {
    val id = integer("id").autoIncrement()
    val phoneNumber = varchar("phone_number", 15)
    val clientName = varchar("client_name", 100)
    val date = varchar("date",15) // Stores LocalDate
    val time = varchar("time",15) // Stores LocalTime
    val status = varchar("status", 20).default("approved")
    val note = text("note").nullable()

    override val primaryKey = PrimaryKey(id)
}

