package com.example.routes.messaging.model

import com.example.core.ConfigureAIModel
import kotlinx.serialization.Serializable



val schema = AgentAction(
    name = "agent_action",
    schema = AgentSchema(
        type = "object",
        properties = AgentProperties(
            action = ActionProperty(
                type = "string",
                description = "The action to be taken by the AI agent.",
                enum = ConfigureAIModel.Actions.actions
            ),
            parameters = ParametersProperty(
                type = "object",
                description = "Parameters relevant to the action specified. This can include any data needed for the action.",
                properties = ConfigureAIModel.Properties.properties, // This will be dynamically populated or left flexible
                additionalProperties = true // Allows for any additional parameters
            ),
            user_message = UserMessageProperty(
                type = "string",
                description = "A message to return to the client or user."
            )
        ),
        required = listOf("action", "user_message", "parameters"),
        additionalProperties = false
    ),
    strict = true
)

val responseFormat = ResponseFormat(json_schema = schema, type = "json_schema")

@Serializable
data class ResponseFormat(
    val type: String,
    val json_schema: AgentAction,
)

@Serializable
data class AgentAction(
    val name: String,
    val schema: AgentSchema,
    val strict: Boolean
)

@Serializable
data class AgentSchema(
    val type: String,
    val properties: AgentProperties,
    val required: List<String>,
    val additionalProperties: Boolean
)

@Serializable
data class AgentProperties(
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
    val properties: Map<String, PropertyField>, // Changed to Map to allow dynamic parameters
    val required: List<String>? = null,
    val additionalProperties: Boolean
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