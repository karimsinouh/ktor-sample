package routes.users.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.domain.UsersRepositoryImpl
import com.example.routes.users.model.UserModel
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Routing.usersRouting(repo: UsersRepository){

    get("users/get/{id}") {
        try {
            val id=call.parameters["id"]
            repo.getUserById(
                id=id,
                onSuccess = {user->
                    successResponse(user)
                },
                onFailure = ::failureResponse
            )
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    get("users/get")  {
        try {
            repo.getAllUsers(::successResponse, ::failureResponse)
        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    post("users/update") {
        try {
            val user=call.receive<UserModel>()

            print(user.toString())

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

    delete("users/delete/{id}"){
        try {
            val id=call.parameters["id"]
            repo.deleteById(
                id=id,
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