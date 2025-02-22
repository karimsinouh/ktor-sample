package com.example.routes.messaging.data

import com.example.routes.messaging.data.GenerateAIResponse.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.users.model.UserModel
import com.example.routes.messaging.model.MessageModel
import com.example.routes.messaging.model.toAIMessages
import com.example.routes.users.model.UsersRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.Serializable

class GenerateAIResponse(
    private val client:HttpClient,
    private val usersRepository: UsersRepository
) {

    private val key="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"
    private val model="gpt-4o-mini"

    suspend operator fun invoke(
        messages:List<MessageModel>,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit,
    ){


        try {

            val recentMessages=getRecentMessages(messages)

            val response=client.post{
                url("https://api.openai.com/v1/chat/completions")
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                    append(HttpHeaders.Authorization,"Bearer $key")
                }
                val body= RequestBody(model=model,messages=recentMessages)
                setBody(body)
            }

            if (response.status.value==200){
                val responseBody: ResponseBody =response.body()
                val choices=responseBody.choices
                if (choices.isEmpty())
                    onFailure("Empty choices")
                else
                    onSuccess(choices[0].message.content)

            }else{
                onFailure(response.toString())
            }


        }catch (e:Exception){
            onFailure(e.message?:"")
        }



    }

    private fun getRecentMessages(
        messages: List<MessageModel>
    ):List<AIMessage>{

        val phoneNumber=messages.last().phoneNumber
        val user=usersRepository.getUserByPhoneNumber(phoneNumber)

        val recentMessages=messages.toAIMessages().toMutableList()

        val trainingMessage=getTrainingMessage(user)
        recentMessages.add(0,trainingMessage)

        return recentMessages
    }

    private fun getTrainingMessage(user:UserModel?): AIMessage {
        val trainingMessageBuilder=StringBuilder()
        trainingMessageBuilder.apply {
            append("You're an assistant integrated in Whatsapp. ")
            append("You work for a dentist to remind customers about their appointments and collect their feedbacks on the service.")
            if (user!=null)
                append("Here's some information about the customer: $user ")
        }

        val trainingMessage= AIMessage(
            role = ROLE_DEVELOPER,
            content = trainingMessageBuilder.toString()
        )
        return trainingMessage
    }

    @Serializable
    data class AIMessage(
        val role:String,
        val content:String
    ){
        companion object{
            const val ROLE_USER="user"
            const val ROLE_ASSISTANT="assistant"
            const val ROLE_DEVELOPER="developer"
        }
    }

    @Serializable
    data class RequestBody(
        val model:String,
        val messages:List<AIMessage>,
    )

    @Serializable
    data class ResponseBody(
        val error: APIError?=null,
        val choices:List<APIChoice>
    )

    @Serializable
    data class APIChoice(
        val message: AIMessage,
    )
    @Serializable
    data class APIError(
        val message:String,
    )

}