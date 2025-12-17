package features.messaging.data

import com.example.routes.messaging.data.SendWhatsappMessageImpl
import features.messaging.domain.MessagesRepository

data class ChatRepository(
    val messages: MessagesRepository,
    val sendWhatsappMessage: SendWhatsappMessageImpl,
)