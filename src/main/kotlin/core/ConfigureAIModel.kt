package com.example.core

import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.model.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.messaging.model.PropertyField
import com.example.routes.users.model.UserModel
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

object ConfigureAIModel {

    object Actions {

        const val SAVE_CLIENT_DETAILS = "save_client_details"
        const val RETRIEVE_CLIENT_INFORMATION = "retrieve_client_information"
        const val NORMAL_CHAT_MESSAGE = "normal_chat_message"

        val actions = listOf(
            SAVE_CLIENT_DETAILS,
            RETRIEVE_CLIENT_INFORMATION,
            NORMAL_CHAT_MESSAGE
        )
    }

    object Properties{

        val properties= mapOf(
            "client_name" to PropertyField("string","name of client"),
            "age" to PropertyField("string","age of the client"),
            "pack" to PropertyField("string","pack or plan that the client chose [In English]"),
            "option" to PropertyField("string","whether the client registered himself or his child.", enum = listOf("adult","child")),
        )

        @Serializable
        data class Parameters(
            val client_name:String?=null,
            val age:String?=null,
            val pack: String?=null,
            val option: String?=null,
        )

    }

    const val AI_API_KEY=""
    const val AI_MODEL="gpt-4o-mini"


    const val TRAINING_MESSAGE="""  

You're a whatsapp bot that helps adults register themselves or their children in Fezari Chess Academy.
After greeting the client, ask him about the preferred language. If the user writes in Moroccan Darija or Arabic, reply in Modern Standard Arabic (Fus-ha).
Then, ask him whether he wants to register himself or his child.
If he wants to register his child, ask him about his child's age.
Show the appropriate plans according to age of the child. Show +15 for adults:

**6–9 years old:**   - Pack Starter: 2 sessions/week (400 MAD/month)   - Pack Plus: 3 sessions/week (600 MAD/month)   - Pack Premium: 4 sessions/week (800 MAD/month)  
**10–14 years old:**   - Pack Starter: 2 sessions/week (500 MAD/month)   - Pack Plus: 3 sessions/week (700 MAD/month)   - Pack Premium: 4 sessions/week (900 MAD/month)  
**15+ years old:**   - Pack Starter: 2 sessions/week (600 MAD/month)   - Pack Plus: 3 sessions/week (800 MAD/month)   - Pack Premium: 4 sessions/week (1000 MAD/month) 

Finally, to confirm with the client, ask him about his full name or his child's full name.
   Do not hallucinate. If you don't have enough info to register the client, simply ask for them
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