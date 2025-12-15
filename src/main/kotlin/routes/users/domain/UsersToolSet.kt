package com.example.routes.users.domain

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.UserModel
import kotlinx.coroutines.CompletableDeferred

@LLMDescription("tools needed to retrieve info or register users in database")
class UsersToolSet(
    private val repo: UsersRepository,
    private val userPhoneNumber: String
) : ToolSet {

    @Tool
    @LLMDescription("retrieves user information from database")
    suspend fun getUserByPhoneNumber(): UserModel? {
        println("DEBUG: Tool 'getUserByPhoneNumber' called for $userPhoneNumber")
        return repo.getUserByPhoneNumber(userPhoneNumber)
    }

    @Tool
    @LLMDescription("register user info in database")
    suspend fun insertUser(
        @LLMDescription("user's full name") name: String,
        @LLMDescription("user's age") age: String,
        @LLMDescription("whether he's registering himself or his child") option: String,
        @LLMDescription("the pack that the user has chosen") pack: String,
    ): String {
        println("DEBUG: Tool 'insertUser' called. Name: $name, Phone: $userPhoneNumber")

        // We use a Deferred to wait for the callback result inside this suspend function
        val deferredResult = CompletableDeferred<String>()

        repo.insertFromAgentResponse(
            phoneNumber = userPhoneNumber,
            name = name,
            age = age,
            option = option,
            pack = pack,
            onSuccess = {
                println("DEBUG: DB Write Success")
                deferredResult.complete("User registered successfully.")
            },
            onFailure = { errorMsg ->
                println("DEBUG: DB Write FAILED: $errorMsg")
                // Propagate the error to the Agent so it knows it failed
                deferredResult.completeExceptionally(Exception("Database Error: $errorMsg"))
            }
        )

        // This will throw the exception if onFailure was called,
        // forcing the Agent to see the error.
        return deferredResult.await()
    }
}