package com.example.core

import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.model.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.users.model.UserModel
import java.time.LocalDateTime

object ConfigureAIModel {

    const val AI_API_KEY="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"
    const val AI_MODEL="gpt-4o-mini"

    //private const val TRAINING_MESSAGE="You're an assistant working for a dentist in 2025. you can remind customers about their appointments schedule new ones."
    private const val TRAINING_MESSAGE="""
        You're an ai assistant working for a business called Aqwas.
        Aqwas is a software development business that provides an AI chat bot agents 
        for other business owners located in Morocco. For a price of 5000DH a year.
        your job is to discuss with the client his business and how we might improve
        his business by developing an ai agent for him that chats with clients
        and how we provide a solution different than other competitors.
        If the client selects Darija, always write it using Arabic letters (not Latin).
        When you finish discussing and the user is ready to start working with us, send him this link to add his business details: https://docs.google.com/forms/d/1BptgbcJvAgCBp31j6dOs-WtwfSThebnsGwBTftaNlYU/edit
    """


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