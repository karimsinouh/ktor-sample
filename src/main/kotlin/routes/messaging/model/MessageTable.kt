package com.example.routes.messaging.model

import org.jetbrains.exposed.sql.Table

object MessageTable:Table("messages") {
    val id =integer("id").autoIncrement()
    val sender=varchar("sender",100)
    val message=text("message")
    val phoneNumber=varchar("phone_number",15)
    val timestamp = long("timestamp")

    override val primaryKey: PrimaryKey=PrimaryKey(id)

}