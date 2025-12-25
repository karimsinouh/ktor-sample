package features.messaging.data

import features.messaging.domain.MessagesRepository
import com.example.features.messaging.model.MessageModel
import com.google.cloud.firestore.Query
import com.google.firebase.cloud.FirestoreClient

class MessagesRepositoryImpl: MessagesRepository {


    private val db = FirestoreClient.getFirestore()

    override suspend fun insert(sender: String, message: String, userPhoneNumber: String) {
        val messagesCollection = db.collection("users")
            .document(userPhoneNumber)
            .collection("messages")

        val messageModel= MessageModel(sender, message, userPhoneNumber, System.currentTimeMillis())
        messagesCollection.add(messageModel)
    }

    override suspend fun getLastMessages(userPhoneNumber: String,limit: Int?): List<MessageModel> {
        val messagesCollection = db.collection("users")
            .document(userPhoneNumber)
            .collection("messages")
            .limit(limit?:15)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .get()

        return if (messagesCollection.documents.isNotEmpty()){
            messagesCollection.toObjects(MessageModel::class.java)
        }else
            emptyList()

    }

    override suspend fun clearChatHistory(userPhoneNumber: String) {
        val ref=db.collection("users")
            .document(userPhoneNumber)
            .collection("messages")

        db.recursiveDelete(ref)

    }
}