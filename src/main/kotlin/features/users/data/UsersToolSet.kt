package features.users.data

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.routes.users.model.UserModel
import features.users.domain.UsersRepository
import kotlinx.coroutines.CompletableDeferred

@LLMDescription("tools needed to register users in database")
class UsersToolSet(
    private val repo: UsersRepository,
    private val userPhoneNumber: String
) : ToolSet {

    @Tool
    @LLMDescription("register user info in database only if all 4 info are available (name, age, option,pack)")
    suspend fun insertUser(
        @LLMDescription("user's full name") name: String,
        @LLMDescription("user's age") age: String,
        @LLMDescription("whether he's registering himself or his child") option: String,
        @LLMDescription("the pack that the user has chosen") pack: String,
    ): String {
        println("DEBUG: Tool 'insertUser' called. Name: $name, Phone: $userPhoneNumber")

        // We use a Deferred to wait for the callback result inside this suspend function
        val deferredResult = CompletableDeferred<String>()

        val user= UserModel(
            phoneNumber = userPhoneNumber,
            name = name,
            age = age,
            option = option,
            pack = pack,
            status = "pending",
            time = System.currentTimeMillis()
        )

        repo.insert(
            user=user,
        )

        println("DEBUG: DB Write Success")
        deferredResult.complete("User registered successfully.")
        // This will throw the exception if onFailure was called,
        // forcing the Agent to see the error.
        return deferredResult.await()
    }
}