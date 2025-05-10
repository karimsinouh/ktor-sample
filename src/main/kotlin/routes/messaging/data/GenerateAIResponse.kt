package com.example.routes.messaging.data

import com.example.core.ConfigureAIModel
import com.example.routes.appointments.model.AppointmentModel
import com.example.routes.appointments.model.AppointmentsRepository
import com.example.routes.messaging.model.*
import com.example.routes.messaging.model.AIMessage.Companion.ROLE_DEVELOPER
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.UserModel
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import org.bson.types.ObjectId

class GenerateAIResponse(
    private val client:HttpClient,
    private val usersRepository: UsersRepository,
    private val appointmentsRepository: AppointmentsRepository,
) {


    suspend operator fun invoke(
        clientPhoneNumber:String,
        messages:List<MessageModel>,
        onSendMessage: suspend (String?) -> Unit,
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
                    append(HttpHeaders.Authorization,"Bearer ${ConfigureAIModel.AI_API_KEY}")
                }
                val body= RequestBody(
                    model = ConfigureAIModel.AI_MODEL,
                    messages = recentMessages,
                    response_format = responseFormat
                )
                setBody(body)
            }




           validateResponse(
               response=response,
               onSuccess = {structuredResponse->

                   handleActions(
                       clientPhoneNumber=clientPhoneNumber,
                       structuredResponse=structuredResponse,
                       user=user,
                       onSendMessage=onSendMessage,
                       onSuccess=onSuccess,
                       onFailure=onFailure
                   )

               },
               onFailure=onFailure
           )


        }catch (e:Exception){
            onFailure(e.message?:"")
        }



    }

    private suspend fun handleActions(
        clientPhoneNumber: String,
        structuredResponse: StructuredResponseBody,
        user:UserModel?,
        onSendMessage:suspend (String?)->Unit,
        onSuccess: suspend (String) -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        //Act according to the action
        when(structuredResponse.action){

            Actions.NORMAL_CHAT_MESSAGE-> onSuccess(structuredResponse.user_message?:"")

            Actions.RETRIEVE_APPOINTMENTS->{
                onSendMessage(structuredResponse.user_message)
                retrieveAppointments(
                    clientPhoneNumber,
                    onSuccess,
                    onFailure
                )
            }

            Actions.SCHEDULE_APPOINTMENT-> scheduleAppointment(
                structuredResponse,
                clientPhoneNumber,
                user,
                onSuccess,
                onFailure
            )

            else-> onSuccess(structuredResponse.user_message?:"")

        }
    }

    private suspend fun scheduleAppointment(
        structuredResponse: StructuredResponseBody,
        clientPhoneNumber: String,
        user:UserModel?,
        onSuccess: suspend (String) -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        appointmentsRepository.insert(
            appointmentModel = AppointmentModel(
                "",
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

    private suspend fun retrieveAppointments(
        clientPhoneNumber: String,
        onSuccess: suspend (String) -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        appointmentsRepository.getByPhoneNumber(
            phoneNumber = clientPhoneNumber,
            onSuccess = {
                onSuccess(AppointmentModel.listToText(it))
            },
            onFailure=onFailure
        )
    }

    private suspend fun validateResponse(
        response:HttpResponse,
        onSuccess: suspend (StructuredResponseBody) -> Unit,
        onFailure: suspend (String) -> Unit
    ){

        try {

            val responseBody: ResponseBody =response.body()

            //API Response
            if (response.status.value==200 && responseBody.choices!=null){

                val structuredResponse=Json.decodeFromString<StructuredResponseBody>(
                    responseBody.choices[0].message.content
                )
                onSuccess(structuredResponse)

            }else {
                onFailure(responseBody.error?.message?:response.status.description)
            }
        }catch (e:Exception){
            onFailure(e.message?:"Failure validating response")
        }

    }

    private fun getRecentMessages(
        user:UserModel?,
        messages: List<MessageModel>
    ):List<AIMessage>{

        val recentMessages=messages.map { it.toAiMessage() }.toMutableList()

        val trainingMessage=ConfigureAIModel.getTrainingMessage(user)
        recentMessages.add(0,trainingMessage)

        return recentMessages
    }


}