package features.messaging.domain

import com.example.features.messaging.model.MessageModel

interface MessagesRepository {

    suspend fun insert(
        sender:String,
        message:String,
        userPhoneNumber:String,
    )

    suspend fun getLastMessages(
        userPhoneNumber: String,
        limit: Int?
    ):List<MessageModel>

}