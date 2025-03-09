package com.example.routes.messaging.model

import kotlinx.serialization.Serializable
import javax.swing.Action

object Actions{
    const val RETRIEVE_APPOINTMENTS="retrieve_appointments"
    const val SCHEDULE_APPOINTMENT="schedule_appointment"
    const val NORMAL_CHAT_MESSAGE="normal_chat_message"

    val actions= listOf(RETRIEVE_APPOINTMENTS, SCHEDULE_APPOINTMENT, NORMAL_CHAT_MESSAGE)

}

val schema=AppointmentAction(
    name="appointment_action",
    schema = AppointmentSchema(
        type = "object",
        properties = AppointmentProperties(
            action = ActionProperty(
                type = "string",
                description = "The action to be taken regarding appointments.",
                enum = Actions.actions
            ),
            parameters = ParametersProperty(
                type = "object",
                description = "Parameters relevant to the action specified.",
                properties = ParametersProperties(
                    date = PropertyField(type = "string", description = "The date for the appointment, formatted as YYYY-MM-DD."),
                    time = PropertyField(type = "string", description = "The time for the appointment, formatted as HH:mm."),
                    note = PropertyField(type = "string", description = "a note about this appointment"),
                ),
                additionalProperties = false,
                required = listOf("date","time","note")
            ),
            user_message = UserMessageProperty(
                type="string",
                description = "A message to return to the client."
            )
        ),
        required = listOf("action","user_message","parameters"),
        additionalProperties = false
    ),
    strict = true
)

val responseFormat=ResponseFormat(json_schema = schema, type = "json_schema")

@Serializable
data class ResponseFormat(
    val type:String,
    val json_schema:AppointmentAction,
)

@Serializable
data class AppointmentAction(
    val name: String,
    val schema: AppointmentSchema,
    val strict: Boolean
)

@Serializable
data class AppointmentSchema(
    val type: String,
    val properties: AppointmentProperties,
    val required: List<String>,
    val additionalProperties: Boolean
)

@Serializable
data class AppointmentProperties(
    val action: ActionProperty,
    val parameters: ParametersProperty,
    val user_message: UserMessageProperty
)

@Serializable
data class ActionProperty(
    val type: String,
    val description: String,
    val enum: List<String>
)

@Serializable
data class ParametersProperty(
    val type: String,
    val description: String,
    val properties: ParametersProperties,
    val required: List<String>?=null,
    val additionalProperties: Boolean
)

@Serializable
data class ParametersProperties(
    val date: PropertyField,
    val time: PropertyField,
    val note: PropertyField
)

@Serializable
data class PropertyField(
    val type: String,
    val description: String
)

@Serializable
data class UserMessageProperty(
    val type: String,
    val description: String
)