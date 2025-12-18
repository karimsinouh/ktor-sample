package com.example.core

object Env {

    val WHATSAPP_ACCESS_TOKEN=System.getenv("WHATSAPP_ACCESS_TOKEN")
    val WHATSAPP_PHONE_NUMBER_ID=System.getenv("WHATSAPP_PHONE_NUMBER_ID")
    val WHATSAPP_VERIFICATION_TOKEN=System.getenv("WHATSAPP_VERIFICATION_TOKEN")

    val GEMINI_KEY=System.getenv("GEMINI_KEY") ?: throw IllegalStateException("Environment variable 'GEMINI_KEY' is not set")
    val TRAINING_MESSAGE=System.getenv("TRAINING_MESSAGE")?:  throw IllegalStateException("Environment variable 'TRAINING_MESSAGE' is not set")

    val FIREBASE_SERVICE_ACCOUNT_JSON=System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON")?:  throw IllegalStateException("Environment variable 'FIREBASE_SERVICE_ACCOUNT_JSON' is not set")


}