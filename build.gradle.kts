
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.serialization)
}

group = "com.example"
version = "0.0.1"


application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation("io.ktor:ktor-server-resources:3.0.3")
    implementation("io.ktor:ktor-server-content-negotiation:3.0.3")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.0.3")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")


    //database
    implementation("org.postgresql:postgresql:42.7.2") // PostgreSQL driver
    implementation("org.jetbrains.exposed:exposed-core:0.59.0") // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-dao:0.59.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.59.0")


    //MongoDB
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")


    //http client
    implementation("io.ktor:ktor-client-core:3.1.0")
    implementation("io.ktor:ktor-client-cio:3.1.0")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.3") // For ContentNegotiation

    //koog
    implementation("ai.koog:koog-ktor:0.5.4")

    implementation("com.google.firebase:firebase-admin:9.7.0")

}
