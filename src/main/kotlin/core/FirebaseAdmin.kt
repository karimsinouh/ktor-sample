package com.example.core

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions

object FirebaseAdmin {

    fun init() {
        val serviceAccountJson = Env.FIREBASE_SERVICE_ACCOUNT_JSON
            ?: throw IllegalStateException("FIREBASE_SERVICE_ACCOUNT_JSON not set")

        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccountJson.byteInputStream()))
            .build()
        FirebaseApp.initializeApp(options)
    }
}