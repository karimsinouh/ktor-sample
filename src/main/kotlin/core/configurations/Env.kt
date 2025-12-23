package com.example.core

object Env {

    val WHATSAPP_ACCESS_TOKEN=System.getenv("WHATSAPP_ACCESS_TOKEN")
    val WHATSAPP_PHONE_NUMBER_ID=System.getenv("WHATSAPP_PHONE_NUMBER_ID")
    val WHATSAPP_VERIFICATION_TOKEN=System.getenv("WHATSAPP_VERIFICATION_TOKEN")

    val FIREBASE_SERVICE_ACCOUNT_JSON=System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")?:  throw IllegalStateException("Environment variable 'FIREBASE_SERVICE_ACCOUNT_JSON' is not set")


}