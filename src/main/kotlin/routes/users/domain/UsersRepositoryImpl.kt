package com.example.routes.users.domain

import com.example.routes.messaging.model.StructuredResponseBody
import com.example.routes.users.data.UsersRepository
import com.example.routes.users.model.User
import com.example.routes.users.model.UserModel
import com.mongodb.client.model.Filters
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.*
import org.bson.types.ObjectId


class UsersRepositoryImpl(
    private val mongoDatabase: MongoDatabase
): UsersRepository {

    override suspend fun insert(
        user: UserModel?,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        try {

            val userRequest=user?.toRequest()

            if (userRequest==null){
                onFailure("Invalid user")
                return
            }

            val result=mongoDatabase.getCollection<User>("users").insertOne(userRequest)
            onSuccess()

        } catch (e:Exception){
            onFailure("From model: ${e.message}"?:"Couldn't insert user")
        }
    }

    override suspend fun insertFromAIResponse(
        phoneNumber:String,
        structuredResponseBody: StructuredResponseBody,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) {

        val timestamp=System.currentTimeMillis()

        val name=structuredResponseBody.parameters?.client_name?:"Unknown"
        val age=structuredResponseBody.parameters?.age?:"Unspecified"
        val pack=structuredResponseBody.parameters?.pack?:"Unspecified"
        val option=structuredResponseBody.parameters?.option?:"Unspecified"

        val client=UserModel(
            id=null,
            name = name,
            phoneNumber = phoneNumber,
            status = "pending",
            age = age,
            pack=pack,
            option=option,
            time=timestamp
        )
        insert(client,onSuccess,onFailure)
    }

    override suspend fun insertFromAgentResponse(
        phoneNumber: String,
        name:String,
        age:String,
        option: String,
        pack: String,
        onSuccess: suspend () -> Unit,
        onFailure: suspend (String) -> Unit
    ) {

        val timestamp=System.currentTimeMillis()

        val client=UserModel(
            id=null,
            name = name,
            phoneNumber = phoneNumber,
            status = "pending",
            age = age,
            pack=pack,
            option=option,
            time=timestamp
        )
        insert(
            user = client,
            onSuccess = {},
            onFailure = {}
        )
    }

    override suspend fun getUserByPhoneNumber(
        phoneNumber:String?,
        onSuccess:suspend (UserModel)->Unit,
        onFailure:suspend (String)->Unit
    ){
        if (phoneNumber==null){
            onFailure("Invalid phone number")
            return
        }

        try {

            val result=mongoDatabase.getCollection<User>("users")
                .find<User>(Filters.eq("phoneNumber",phoneNumber))
                .firstOrNull()
            if (result!=null)
                onSuccess(result.toModel())
            else
                onFailure("Couldn't find user")

        }catch (e:Exception){
            onFailure(e.message?:"Failed to retrieve user")
        }
    }


    override suspend fun getUserByPhoneNumber(
        phoneNumber: String?,
    ): UserModel?{
        return try {

            mongoDatabase.getCollection<User>("users")
                .find<User>(Filters.eq("phoneNumber",phoneNumber))
                .firstOrNull()
                ?.toModel()

        }catch (e:Exception){
            null
        }
    }

    override suspend fun getUserById(
        id: String?,
        onSuccess: suspend (UserModel) -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        if (id==null){
            onFailure("Invalid phone number")
            return
        }

        try {

            val objectId= ObjectId(id)

            val result=mongoDatabase.getCollection<User>("users")
                .find<User>(Filters.eq("_id",objectId))
                .firstOrNull()
            if (result!=null)
                onSuccess(result.toModel())
            else
                onFailure("Couldn't find user")

        }catch (e:Exception){
            onFailure(e.message?:"Failed tor retrieve user")
        }
    }

    override suspend fun getAllUsers(
        onSuccess:suspend (List<UserModel>)->Unit,
        onFailure:suspend (String)->Unit
    )=try {

        val result=mongoDatabase.getCollection<User>("users")
            .find<User>()
            .limit(30)
            .toList()

        val users=result.map {
            it.toModel()
        }
        onSuccess(users)

    }catch (e:Exception){
        onFailure(e.message?:"Couldn't retrieve users")
    }

    override suspend fun deleteByPhoneNumber(
        phoneNumber: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    ){
        if (phoneNumber==null){
            onFailure("Invalid phone number")
            return
        }

        try {

            val result=mongoDatabase.getCollection<User>("users")
                .deleteOne(Filters.eq("phoneNumber",phoneNumber))

            if (result.deletedCount>0)
                onSuccess()
            else
                onFailure("No user was deleted")

        }catch (e:Exception){
            onFailure(e.message?:"Failed tor retrieve user")
        }
    }

    override suspend fun deleteById(
        id: String?,
        onFailure: suspend (String) -> Unit,
        onSuccess: suspend () -> Unit
    ){
        if (id==null){
            onFailure("Invalid user id")
            return
        }

        try {

            val result=mongoDatabase.getCollection<User>("users")
                .deleteOne(Filters.eq("id",id))

            if (result.deletedCount>0)
                onSuccess()
            else
                onFailure("No user was deleted")

        }catch (e:Exception){
            onFailure(e.message?:"Failed tor retrieve user")
        }
    }

    override suspend fun update(
        user: UserModel?,
        onSuccess:suspend  () -> Unit,
        onFailure: suspend (String) -> Unit
    ){
        try {

            val objectId= ObjectId(user?.id)
            val query=Filters.eq("_id",objectId)

            val updates=Updates.combine(
                Updates.set(User::name.name,user?.name),
                Updates.set(User::phoneNumber.name,user?.phoneNumber),
                Updates.set(User::status.name,user?.status),
                Updates.set(User::pack.name,user?.pack),
            )
            val options=UpdateOptions().upsert(true)

            val result=mongoDatabase.getCollection<User>("users")
                .updateOne(query,updates,options)

            if(result.modifiedCount>0)
                onSuccess()
            else
                onFailure("No user was updated")

        }catch (e:Exception){
            onFailure(e.message?:"Couldn't update user")
        }
    }

}