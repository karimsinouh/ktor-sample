package com.example.messaging.data

import com.example.database.model.User

class GetUser {

    fun byPhoneNumber(
        phoneNumber: String,
        onSuccess: (User) -> Unit,
        onFailure:(String)->Unit
    ){
        onSuccess(
            User(0,"Karim","+212677198667","")
        )
    }

}