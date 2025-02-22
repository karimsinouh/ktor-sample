package com.example.routes.users.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.users.model.UserModel
import com.example.routes.users.model.UsersRepository
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Routing.usersRouting(repo: UsersRepository){

    get("users/get/{phoneNumber}") {
        try {
            val phoneNumber=call.parameters["phoneNumber"]
            repo.getUserByPhoneNumber(
                phoneNumber=phoneNumber,
                onSuccess = {user->
                    successResponse(user)
                },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    get("users/get") {
        try {
            repo.getAllUsers(::successResponse, ::failureResponse)
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    post("users/update") {
        try {
            val user=call.receive<UserModel>()

            repo.update(
                user=user,
                onSuccess = {
                    successResponse("User successfully inserted")
                },
                onFailure = ::failureResponse
            )

        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    put("users/insert"){
        try {
            val user=call.receive<UserModel>()

            repo.insert(
                user=user,
                onSuccess = {
                    successResponse("User successfully inserted")
                },
                onFailure = ::failureResponse
            )

        }catch (e:IllegalStateException){
            successResponse("User successfully inserted")
        }catch (e:Exception){
            failureResponse("From presentation: ${e.message}"?:"")
        }
    }

    delete("users/delete/{phoneNumber}"){
        try {
            val phoneNumber=call.parameters["phoneNumber"]
            repo.deleteByPhoneNumber(
                phoneNumber=phoneNumber,
                onSuccess = {
                    successResponse("User deleted")
                },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

}