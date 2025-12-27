package com.example.core

import com.example.features.errorsLog.model.ErrorLogModel
import com.example.routes.users.model.UserModel
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

object SendNotification {

    fun userRegistration(user: UserModel): Result<Unit>{
        return try {
            val message = Message.builder()
                .setTopic("admin_alerts") // Must match Android subscription
                .setNotification(
                    Notification.builder()
                        .setTitle("New User Registered")
                        .setBody("${user.name} just signed up!")
                        .build()
                )
                .build()

            FirebaseMessaging.getInstance().send(message)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    fun error(error: ErrorLogModel){
        val message = Message.builder()
            .setTopic("admin_alerts") // Must match Android subscription
            .setNotification(
                Notification.builder()
                    .setTitle("Your server crashed")
                    .setBody(error.stackTrace)
                    .build()
            )
            .build()

        FirebaseMessaging.getInstance().send(message)
    }

}