package com.example.routes.users.model

import org.jetbrains.exposed.sql.Table

object UsersTable: Table("users") {

    val id=integer("id").autoIncrement()
    val name=varchar("name",100)
    val phoneNumber=varchar("phone_number",15)
    val email = varchar("email", 100).nullable() // Make email nullable
    val note = text("note").nullable() // Make note nullable
    val feedbackCollected = bool("feedback_collected").default(false) // Add default value

    override val primaryKey=PrimaryKey(id)

}