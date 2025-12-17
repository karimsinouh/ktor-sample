package features.users.domain

import com.example.routes.users.model.UserModel

interface UsersRepository {

    suspend fun insert(user: UserModel?) // Throws exception if fails
    suspend fun getUserByPhoneNumber(phoneNumber: String?): UserModel?
    suspend fun getAllUsers(): List<UserModel>
    suspend fun update(user: UserModel?)
    suspend fun deleteByPhoneNumber(phoneNumber: String?)

}