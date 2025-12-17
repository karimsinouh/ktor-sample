package features.users.data

import com.example.routes.users.model.UserModel
import com.google.firebase.cloud.FirestoreClient
import features.users.domain.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryFirebaseImpl : UsersRepository {

    private val db = FirestoreClient.getFirestore()
    private val usersCollection = db.collection("users")

    override suspend fun insert(
        user: UserModel?,
    ) = withContext(Dispatchers.IO) {

        if (user == null || user.phoneNumber == null) {
            throw IllegalArgumentException("User must not be null")
        }

        usersCollection.document(user.phoneNumber).set(user).get()

        Unit
    }



    override suspend fun getUserByPhoneNumber(phoneNumber: String?): UserModel? = withContext(Dispatchers.IO) {
        if (phoneNumber == null) return@withContext null

        val querySnapshot = usersCollection
            .document(phoneNumber)
            .get().get()

        return@withContext if (querySnapshot.exists()) {
            querySnapshot.toObject(UserModel::class.java)
        } else
            null
    }

    override suspend fun getAllUsers():List<UserModel> = withContext(Dispatchers.IO) {
        val querySnapshot = usersCollection.get().get()
        val users = querySnapshot.toObjects(UserModel::class.java)
        return@withContext users
    }

    override suspend fun deleteByPhoneNumber(
        phoneNumber: String?,
    )  {
        withContext(Dispatchers.IO) {
            if (phoneNumber == null)
                throw IllegalArgumentException("phone number must not be null")

            usersCollection.document(phoneNumber).delete().get()
        }

    }

    override suspend fun update(
        user: UserModel?,
    ){


        if (user == null) {
            throw IllegalArgumentException("User or User ID is missing")
        }

        withContext(Dispatchers.IO) {
            usersCollection.document(user.phoneNumber ?: "").set(user).get()
        }

    }

}