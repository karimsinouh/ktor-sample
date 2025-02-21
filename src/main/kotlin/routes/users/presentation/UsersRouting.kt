package com.example.routes.users.presentation

import com.example.core.errorResponse
import com.example.core.successResponse
import com.example.routes.users.model.UsersRepository
import io.ktor.server.engine.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Except

fun Routing.usersRouting(repo: UsersRepository){

    get("users/get/{phoneNumber}") {
        try {
            val phoneNumber=call.parameters["phoneNumber"]
            val user=repo.getUserByPhoneNumber(
                phoneNumber=phoneNumber,
                onSuccess = {user->
                    successResponse(user)
                },
                onFailure = {
                    errorResponse(it)
                }
            )
        }catch (e:Exception){
            errorResponse(e.message?:"")
        }
    }

}