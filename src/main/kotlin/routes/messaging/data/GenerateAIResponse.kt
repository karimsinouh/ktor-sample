package com.example.routes.messaging.data

import com.example.core.Constants.AI_API_KEY
import com.example.core.Constants.AI_MODEL
import com.example.routes.appointments.model.AppointmentModel
import com.example.routes.appointments.model.AppointmentsRepository
import com.example.routes.messaging.model.*
import com.example.routes.messaging.model.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.users.model.UserModel
import com.example.routes.users.model.UsersRepository
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class GenerateAIResponse(
    private val client:HttpClient,
    private val usersRepository: UsersRepository,
    private val appointmentsRepository: AppointmentsRepository,
) {


    suspend operator fun invoke(
        clientPhoneNumber:String,
        messages:List<MessageModel>,
        onSuccess:suspend (String)->Unit,
        onFailure:suspend (String)->Unit,
    ){


        try {

            val user=usersRepository.getUserByPhoneNumber(clientPhoneNumber)
            val recentMessages=getRecentMessages(user,messages)

            //API Request
            val response=client.post{
                url("https://api.openai.com/v1/chat/completions")
                headers {
                    append(HttpHeaders.ContentType,"application/json")
                    append(HttpHeaders.Authorization,"Bearer $AI_API_KEY")
                }
                val body= RequestBody(
                    model = AI_MODEL,
                    messages = recentMessages,
                    response_format = responseFormat
                )
                setBody(body)
            }

            val responseBody: ResponseBody =response.body()

            println(responseBody)
            //API Response
            if (response.status.value==200 && responseBody.choices!=null){

                val structuredResponse=Json.decodeFromString<StructuredResponseBody>(responseBody.choices[0].message.content)
                println(structuredResponse)

                //Act according to the action
                when(structuredResponse.action){
                    "normal_chat_message"->{
                        onSuccess(structuredResponse.user_message?:"")
                    }
                    "retrieve_appointments"->{
                        appointmentsRepository.getByPhoneNumber(
                            phoneNumber = clientPhoneNumber,
                            onSuccess = {
                                onSuccess(AppointmentModel.listToText(it))
                            },
                            onFailure=onFailure
                        )
                    }
                    "schedule_appointment"->{
                        appointmentsRepository.insert(
                            appointmentModel = AppointmentModel(
                                0,
                                clientPhoneNumber,
                                user?.name?:"Username unspecified",
                                structuredResponse.parameters?.date?:"",
                                structuredResponse.parameters?.time?:"",
                                status="pending_approval"
                            ),
                            onSuccess={
                                onSuccess(structuredResponse.user_message?:"")
                            },
                            onFailure=onFailure
                        )
                    }
                    else->{
                        onSuccess(structuredResponse.user_message?:"")
                    }
                }

            }else {
                onFailure(responseBody.error?.message?:response.status.description)
            }


        }catch (e:Exception){
            onFailure(e.message?:"")
        }



    }

    private fun getRecentMessages(
        user:UserModel?,
        messages: List<MessageModel>
    ):List<AIMessage>{


        val recentMessages=messages.toAIMessages().toMutableList()

        val trainingMessage=getTrainingMessage(user)
        recentMessages.add(0,trainingMessage)

        return recentMessages
    }

    private fun getTrainingMessage(user:UserModel?): AIMessage {
        val trainingMessageBuilder=StringBuilder()
        trainingMessageBuilder.apply {
            append("You're an assistant working for a dentist. ")
            append("you can remind customers about their appointments schedule new ones.")
            if (user!=null)
                append("some info about the customer: $user ")
        }

        val trainingMessage= AIMessage(
            role = ROLE_DEVELOPER,
            content = trainingMessageBuilder.toString()
        )
        return trainingMessage
    }



}