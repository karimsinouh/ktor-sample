package features.messaging.useCase

import com.example.core.AgentCore
import features.messaging.data.ChatRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class ProcessIncomingWhatsappMessages(
    private val chatRepository: ChatRepository,
    private val agent: AgentCore,
) {

    suspend operator fun invoke(clientPhoneNumber: String, message: String,messagesLimit:Int?) = coroutineScope {
        // 1. Save User Message & Fetch History concurrently
        val historyDeferred = async { chatRepository.messages.getLastMessages(clientPhoneNumber,messagesLimit).reversed() }
        val saveUserMsgJob = async { chatRepository.messages.insert("user", message, clientPhoneNumber) }


        val history = historyDeferred.await()
        // We don't necessarily need to wait for saveUserMsgJob to finish before calling AI

        // 2. Heavy AI Processing
        val aiResponse = agent.run(clientPhoneNumber, message, history)

        // 3. Save AI Response & Send to WhatsApp concurrently
        val saveJob = async {
            chatRepository.messages.insert("assistant", aiResponse, clientPhoneNumber)
        }
        val sendJob = async {
            chatRepository.sendWhatsappMessage(clientPhoneNumber, aiResponse)
        }

        // Wait for both to finish
        saveJob.await()
        sendJob.await()
    }

}