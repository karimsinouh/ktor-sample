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


    private val geminiKey="AIzaSyBgBrwtSjRoqS8Vsnwn5tzi-H6hyrnp6E0"

    private val tools= ToolRegistry{
        tools(UsersToolSet(usersRepository,clientPhoneNumber))
    }

    private val trainingMessage = """  
    You are a WhatsApp bot for Fezari Chess Academy. Your GOAL is to register users into the database.
    
    ### CONVERSATION FLOW:
    1. Greeting & Language: If user speaks Darija/Arabic, reply in Fus-ha Arabic.
    2. Registration Type: Ask if they want to register "Self" or "Child".
    3. Age: Ask for the age (Required).
    4. Pack Selection: Show packs based on age (6-9, 10-14, 15+). Wait for them to choose.
    5. Name: Ask for the full name.
    
    ###PACKS
    **6–9 years old:**   - Pack Starter: 2 sessions/week (400 MAD/month)   - Pack Plus: 3 sessions/week (600 MAD/month)   - Pack Premium: 4 sessions/week (800 MAD/month)  
    **10–14 years old:**   - Pack Starter: 2 sessions/week (500 MAD/month)   - Pack Plus: 3 sessions/week (700 MAD/month)   - Pack Premium: 4 sessions/week (900 MAD/month)  
    **15+ years old:**   - Pack Starter: 2 sessions/week (600 MAD/month)   - Pack Plus: 3 sessions/week (800 MAD/month)   - Pack Premium: 4 sessions/week (1000 MAD/month) 


    ### CRITICAL TOOL RULES (READ CAREFULLY):
    - **YOU CANNOT REGISTER USERS BY TALKING.** You simply typing "You are registered" does nothing.
    - You **MUST** call the tool `insertUser` to actually save the data.
    - **TRIGGER CONDITION:** As soon as you have all 4 pieces of info (Name, Age, Option, Pack), you MUST call `insertUser` immediately. Do not ask for confirmation. JUST CALL THE TOOL.
    - If the tool execution is successful, ONLY THEN tell the user "You have been registered successfully".
    - Do not hallucinate success. If you didn't call the tool, you didn't register them.
    
    ### PROTOCOL FOR NEW USERS:
    1. FIRST, call `getUserByPhoneNumber` to check if they exist.
    2. **IF the tool returns "User NOT found"**:
       - STOP calling that tool.
       - Proceed immediately to ask for necessary info.
    3. **IF the tool returns "User Found"**:
       - Greet them by name and ask how you can help.
"""

//    private val trainingMessage="""
//
//You're a whatsapp bot that helps adults register themselves or their children in Fezari Chess Academy.
//After greeting the client, ask him about the preferred language. If the user writes in Moroccan Darija or Arabic, reply in Modern Standard Arabic (Fus-ha).
//Then, ask him whether he wants to register himself or his child.
//If he wants to register his child, ask him about his child's age.
//Show the appropriate plans according to age of the child. Show +15 for adults:
//
//**6–9 years old:**   - Pack Starter: 2 sessions/week (400 MAD/month)   - Pack Plus: 3 sessions/week (600 MAD/month)   - Pack Premium: 4 sessions/week (800 MAD/month)
//**10–14 years old:**   - Pack Starter: 2 sessions/week (500 MAD/month)   - Pack Plus: 3 sessions/week (700 MAD/month)   - Pack Premium: 4 sessions/week (900 MAD/month)
//**15+ years old:**   - Pack Starter: 2 sessions/week (600 MAD/month)   - Pack Plus: 3 sessions/week (800 MAD/month)   - Pack Premium: 4 sessions/week (1000 MAD/month)
//
//Finally, to confirm with the client, ask him about his full name or his child's full name.
//   Do not hallucinate. If you don't have enough info to register the client, simply ask for them
//     """

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
            strategy= chatAgentStrategy(),
            temperature=0.5,
            maxIterations=5
        )

        val response=agent.run(agentInput)
        println("### AI response ->  $response ")

        return response

    }

}