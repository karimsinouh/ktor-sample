package routes.users.domain

import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.UserModel
import com.google.firebase.cloud.FirestoreClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRepositoryFirebaseImpl : UsersRepository {

    private val db = FirestoreClient.getFirestore()
    private val usersCollection = db.collection("users")

    override suspend fun insert(
        user: UserModel?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {

        if (user == null || user.phoneNumber==null) {
            onFailure("User cannot be null")
            return@withContext
        }

        try {

            usersCollection.document(user.phoneNumber)
                .set(user)
            onSuccess()

        } catch (e: Exception) {
            onFailure(e.message ?: "Couldn't add user")
        }
    }


    override suspend fun insertFromAgentResponse(
        phoneNumber: String,
        name: String,
        age: String,
        option: String,
        pack: String,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) {
        val timeStamp = System.currentTimeMillis()
        val user = UserModel(
            id = "",
            name = name,
            phoneNumber = phoneNumber,
            status = "pending",
            age = age,
            pack = pack,
            option = option,
            time = timeStamp
        )
        insert(user, onSuccess, onFailure)
    }

    override suspend fun getUserByPhoneNumber(
        phoneNumber: String?,
        onSuccess: suspend (UserModel) -> Unit,
        onFailure: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        if (phoneNumber == null) {
            onFailure("Phone number is null")
            return@withContext
        }

        try {
            val querySnapshot = usersCollection
                .whereEqualTo("phoneNumber", phoneNumber)
                .get().get()


            if (querySnapshot.documents.isNotEmpty()) {
                // Convert the first matching document to UserModel
                val user = querySnapshot.documents[0].toObject(UserModel::class.java)
                onSuccess(user)
            } else {
                onFailure("User not found")
            }
        } catch (e: Exception) {
            onFailure(e.message ?: "Error fetching user")
        }
    }

    override suspend fun getUserByPhoneNumber(phoneNumber: String?): UserModel? = withContext(Dispatchers.IO) {
        if (phoneNumber == null) return@withContext null

        try {
            val querySnapshot = usersCollection
                .whereEqualTo("phoneNumber", phoneNumber)
                .get().get()

            if (!querySnapshot.isEmpty) {
                return@withContext querySnapshot.documents[0].toObject(UserModel::class.java)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }

    override suspend fun getUserById(
        id: String?,
        onSuccess: suspend (UserModel) -> Unit,
        onFailure: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        if (id == null) {
            onFailure("ID is null")
            return@withContext
        }

        try {
            val docSnapshot = usersCollection.document(id).get().get()
            if (docSnapshot.exists()) {
                val user = docSnapshot.toObject(UserModel::class.java)
                if (user != null) {
                    onSuccess(user)
                } else {
                    onFailure("Failed to parse user data")
                }
            } else {
                onFailure("User not found")
            }
        } catch (e: Exception) {
            onFailure(e.message ?: "Error fetching user by ID")
        }
    }

    override suspend fun getAllUsers(
        onSuccess: suspend (List<UserModel>) -> Unit,
        onFailure: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            val querySnapshot = usersCollection.get().get()
            val users = querySnapshot.toObjects(UserModel::class.java)
            onSuccess(users)
        } catch (e: Exception) {
            onFailure(e.message ?: "Error fetching all users")
        }
    }

    override suspend fun deleteByPhoneNumber(
        phoneNumber: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    ) = withContext(Dispatchers.IO) {
        if (phoneNumber == null) {
            onFailure("Phone number cannot be null")
            return@withContext
        }

        try {
            // First find the user to get the ID
            val querySnapshot = usersCollection
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .get()

            if (querySnapshot.documents.isNotEmpty()) {
                // Delete all documents matching this phone number (usually just one)
                for (doc in querySnapshot.documents) {
                    usersCollection.document(doc.id).delete()
                }
                onSuccess()
            } else {
                onFailure("User not found to delete")
            }
        } catch (e: Exception) {
            onFailure(e.message ?: "Error deleting user")
        }
    }

    override suspend fun update(
        user: UserModel?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) = withContext(Dispatchers.IO) {
        if (user?.id == null) {
            onFailure("User or User ID is missing")
            return@withContext
        }

        try {
            // We use set() with input user.
            // Warning: This overwrites the whole document.
            // If you only want partial updates, use usersCollection.document(user.id).update(mapOfFields)
            usersCollection.document(user.id).set(user)
            onSuccess()
        } catch (e: Exception) {
            onFailure(e.message ?: "Error updating user")
        }
    }

    override suspend fun deleteById(
        id: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    ) = withContext(Dispatchers.IO) {
        if (id == null) {
            onFailure("ID cannot be null")
            return@withContext
        }

        try {
            usersCollection.document(id).delete()
            onSuccess()
        } catch (e: Exception) {
            onFailure(e.message ?: "Error deleting user")
        }
    }
}