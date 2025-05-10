package com.example.core

import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.model.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.users.model.UserModel
import java.time.LocalDateTime

object ConfigureAIModel {

    const val AI_API_KEY="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"
    const val AI_MODEL="gpt-4o-mini"

    private const val TRAINING_MESSAGE="You're an assistant working for a dentist in 2025. you can remind customers about their appointments schedule new ones."



    fun getTrainingMessage(user:UserModel?):AIMessage{

        val str=StringBuilder()
        val time= LocalDateTime.now()

        str.append(TRAINING_MESSAGE)
        if (user!=null)
            str.append("info about user: $user")
        str.append("current time is $time.")

        val trainingMessage= AIMessage(
            role = ROLE_DEVELOPER,
            content = str.toString()
        )
        return trainingMessage
    }

}