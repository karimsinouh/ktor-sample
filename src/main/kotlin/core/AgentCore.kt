package com.example.core

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.tools
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


    private val geminiKey="AIzaSyBgBrwtSjRoqS8Vsnwn5tzi-H6hyrnp6E0"

    private val tools= ToolRegistry{
        tools(UsersToolSet(usersRepository,clientPhoneNumber))
    }

    private val trainingMessage="""  

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
            promptExecutor = simpleGoogleAIExecutor(geminiKey),
            systemPrompt = trainingMessage,
            llmModel = GoogleModels.Gemini2_0Flash,
            toolRegistry = tools,
        )

        val response=agent.run(agentInput)
        println("### AI response ->  $response ")

        return response

    }

}