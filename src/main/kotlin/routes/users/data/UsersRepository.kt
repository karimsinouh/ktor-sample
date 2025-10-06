package com.example.routes.users.data

import com.example.routes.messaging.model.StructuredResponseBody
import com.example.routes.users.model.UserModel

interface UsersRepository {

    suspend fun insert(
        user: UserModel?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    )

    suspend fun insertFromAIResponse(
        phoneNumber:String,
        structuredResponseBody: StructuredResponseBody,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    )

    suspend fun getUserByPhoneNumber(
        phoneNumber:String?,
        onSuccess:suspend (UserModel)->Unit,
        onFailure:suspend (String)->Unit
    )


    suspend fun getUserByPhoneNumber(
        phoneNumber:String,
    ):UserModel?

    suspend fun getUserById(
        id: String?,
        onSuccess: suspend (UserModel) -> Unit,
        onFailure: suspend (String) -> Unit
    )

    suspend fun getAllUsers(
        onSuccess:suspend (List<UserModel>)->Unit,
        onFailure:suspend (String)->Unit
    )

    suspend fun deleteByPhoneNumber(
        phoneNumber: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    )

    suspend fun update(
        user: UserModel?,
        onSuccess:suspend  () -> Unit,
        onFailure: suspend (String) -> Unit
    )

    suspend fun deleteById(id: String?, onFailure: suspend (String) -> Unit, onSuccess: suspend () -> Unit)
}