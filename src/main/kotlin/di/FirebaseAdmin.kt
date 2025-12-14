package di

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import java.io.InputStream

object FirebaseAdmin {
    private val serviceAccount: InputStream? = 
        this::class.java.classLoader.getResourceAsStream("serviceAccountKey.json")

    fun init() {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        FirebaseApp.initializeApp(options)
    }
}