package features.messaging.useCase

import com.example.core.AgentCore
import features.messaging.data.ChatRepository

class ProcessIncomingWhatsappMessages(
    private val chatRepository: ChatRepository,
    private val agent: AgentCore,
) {

    suspend operator fun invoke(
        clientPhoneNumber:String,
        message: String
    ){
        //get the last 10 messages from this conversation from the database
        val history=chatRepository.messages.getLastMessages(clientPhoneNumber).reversed()

        //store the client message in database
        chatRepository.messages.insert("user",message, clientPhoneNumber)

        //get ai response
        val aiResponse=agent.run(clientPhoneNumber,message,history)

        //store the AI response in the database
        chatRepository.messages.insert(
            sender = "assistant",
            message = aiResponse,
            userPhoneNumber = clientPhoneNumber
        )

        //send the AI response back to user via whatsapp
        chatRepository.sendWhatsappMessage(clientPhoneNumber, aiResponse)
    }

}