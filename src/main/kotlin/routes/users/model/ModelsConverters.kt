package com.example.routes.users.model

import org.jetbrains.exposed.sql.ResultRow

fun ResultRow?.toUserOrNull():UserModel?{
    return if (this==null)
        null
    else
        UserModel(
            id=this[UsersTable.id],
            name = this[UsersTable.name],
            phoneNumber = this[UsersTable.phoneNumber],
            email = this[UsersTable.email],
            note = this[UsersTable.note],
            feedbackCollected = this[UsersTable.feedbackCollected]
        )
}

fun ResultRow.toUser():UserModel{
        return UserModel(
            id=this[UsersTable.id],
            name = this[UsersTable.name],
            phoneNumber = this[UsersTable.phoneNumber],
            email = this[UsersTable.email],
            note = this[UsersTable.note],
            feedbackCollected = this[UsersTable.feedbackCollected]
        )
}