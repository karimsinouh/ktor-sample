package com.example.routes.users.model

import com.example.routes.users.data.UsersDAO

class UsersRepository(
    private val usersDAO: UsersDAO
) {

    suspend fun insert(
        user:UserModel?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        try {

            if(user==null){
                onFailure("Null user data")
                return
            }
            usersDAO.insertUser(user)
            onSuccess()

        }catch (e:Exception){
            onFailure(e.message?:"Couldn't insert user")
        }
    }

    suspend fun getUserByPhoneNumber(
        phoneNumber:String?,
        onSuccess:suspend (UserModel)->Unit,
        onFailure:suspend (String)->Unit
    ){
        if (phoneNumber==null){
            onFailure("Invalid phone number")
            return
        }

        try {
            val user=usersDAO.getUserByPhoneNumber(phoneNumber)
            onSuccess(user)
        }catch (e:Exception){
            onFailure(e.message?:"Failed tor retrieve user")
        }
    }

    suspend fun getAllUsers(
        onSuccess:suspend (List<UserModel>)->Unit,
        onFailure:suspend (String)->Unit
    )=try {
        val users=usersDAO.getAllUsers()
        onSuccess(users)
    }catch (e:Exception){
        onFailure(e.message?:"Couldn't retrieve users")
    }

    suspend fun deleteByPhoneNumber(
        phoneNumber: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    ){
        if (phoneNumber==null){
            onFailure("Invalid phone number")
            return
        }

        try {
            usersDAO.deleteByPhoneNumber(phoneNumber)
            onSuccess()
        }catch (e:Exception){
            onFailure(e.message?:"Failed tor retrieve user")
        }
    }

    suspend fun update(
        user: UserModel?,
        onSuccess:suspend  () -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        try {

            if(user==null){
                onFailure("Null user")
                return
            }

            usersDAO.update(user)
            onSuccess()
        }catch (e:Exception){
            onFailure(e.message?:"Couldn't update user")
        }
    }

}