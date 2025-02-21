package com.example.routes.users.model

import org.jetbrains.exposed.sql.Table

object UsersTable: Table("users") {

    val id=integer("id").autoIncrement()
    val name=varchar("name",100)
    val email=varchar("email",100)
    val phoneNumber=varchar("phone_number",15)
    val note=text("note")
    val feedback_collected=bool("feedback_collected")

    override val primaryKey=PrimaryKey(id)

}