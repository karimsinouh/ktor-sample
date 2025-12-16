package com.example.core

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
import ai.koog.agents.ext.agent.chatAgentStrategy
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.all.simpleGoogleAIExecutor
import ai.koog.prompt.executor.llms.all.simpleOpenAIExecutor
import com.example.routes.messaging.model.AIMessage
import com.example.routes.messaging.model.MessageModel
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.domain.UsersToolSet

class AgentCore(
    private val usersRepository: UsersRepository,
    private val clientPhoneNumber: String,
) {


    private val tools= ToolRegistry{
        tools(UsersToolSet(usersRepository,clientPhoneNumber))
    }

    suspend fun run(
        history: List<MessageModel>,
        clientMessage: String,
    ): String{
        println("### message received -> $clientMessage from $clientPhoneNumber ")

        // 1. Prepare the Input (Context + Task)
        // We format the history manually because the Agent needs a single string to "think" about.
        val context = history.joinToString(separator = "\n") { message ->
            val role = if (message.sender == AIMessage.ROLE_USER) "User" else "Assistant"
            "$role: ${message.message}"
        }

        val agentInput = """
            # HISTORY
            $context
            
            # CURRENT REQUEST
            User: $clientMessage
            """.trimIndent()
        println("\n\nagent input: $agentInput")

        val agent= AIAgent(
            promptExecutor = simpleGoogleAIExecutor(Env.GEMINI_KEY),
            systemPrompt = Env.TRAINING_MESSAGE,
            llmModel = GoogleModels.Gemini2_0Flash,
            toolRegistry = tools
        )

        val response=agent.run(agentInput)
        println("### AI response ->  $response ")

        return response

    }

}