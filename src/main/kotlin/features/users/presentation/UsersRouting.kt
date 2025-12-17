package features.users.presentation

import com.example.core.model.failureResponse
import com.example.core.model.successResponse
import features.users.domain.UsersRepository
import com.example.routes.users.model.UserModel
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Routing.usersRouting(repo: UsersRepository){

    get("users/get/{id}") {
        try {
            val id=call.parameters["id"]
            val user=repo.getUserByPhoneNumber(id)

            if (user==null)
                failureResponse("User not found")
            else
                successResponse(user)

        }catch (e:Exception){
            failureResponse(e.message?:"")
        }
    }

    get("users/get")  {
        val users=repo.getAllUsers()
        successResponse(users)
    }

    post("users/update") {

        val user=call.receive<UserModel>()
        repo.update(user)

    }

    put("users/insert"){
        val user=call.receive<UserModel>()
        repo.insert(user)
    }

    delete("users/delete/{id}"){
        val id=call.parameters["id"]
        repo.deleteByPhoneNumber(id)
    }

}