package com.example.routes.users.domain

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.UserModel

@LLMDescription("tools needed to retrieve info or save users in database")
class UsersToolSet(private val repo: UsersRepository): ToolSet {

    @Tool
    @LLMDescription("retrieves user information from database")
    suspend fun getUserByPhoneNumber(
        @LLMDescription("the user's phone number")
        phoneNumber: String?=null,
    ): UserModel?{
        return repo.getUserByPhoneNumber(phoneNumber)
    }

    @Tool
    @LLMDescription("Saves user info in database")
    suspend fun insertUser(
        phoneNumber: String,
        name:String,
        age:String,
        option: String,
        pack: String,
    ){
        repo.insertFromAgentResponse(phoneNumber,name,age,option,pack,{},{})
    }


}