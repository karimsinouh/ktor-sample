package com.example.core

object Env {

    val WHATSAPP_ACCESS_TOKEN=System.getenv("WHATSAPP_ACCESS_TOKEN")
    val WHATSAPP_PHONE_NUMBER_ID=System.getenv("WHATSAPP_PHONE_NUMBER_ID")
    val WHATSAPP_VERIFICATION_TOKEN=System.getenv("WHATSAPP_VERIFICATION_TOKEN")

    val GEMINI_KEY=System.getenv("GEMINI_KEY")
    val TRAINING_MESSAGE=System.getenv("TRAINING_MESSAGE")


}