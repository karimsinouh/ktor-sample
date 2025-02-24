package com.example.core.model

fun getCorrectPhoneNumberFormat(phoneNumber:String):String{

    return if (phoneNumber.startsWith("+"))
        phoneNumber
    else {
        val str=StringBuilder()
        str.append("+")
        str.append(phoneNumber)
        return str.toString()
    }
}