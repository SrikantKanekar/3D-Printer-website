package com.example.config

import com.example.util.constants.Auth.JWT_AUTH_SECRET
import com.example.util.constants.Notification.NOTIFICATION_EMAIL
import com.example.util.constants.Notification.NOTIFICATION_PASSWORD
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.configSetup(testing: Boolean) {
    val appConfig by inject<AppConfig>()

    //testing
    appConfig.testing = testing

    //JWT
    val jwt = environment.config.config("ktor.jwt")
    val secret = if (testing) "123456" else System.getenv(JWT_AUTH_SECRET)
    val issuer = jwt.property("issuer").getString()
    val audience = jwt.property("audience").getString()
    val realm = jwt.property("realm").getString()
    appConfig.jwtConfig = JWTConfig(secret, issuer, audience, realm)

    // Notification
    val email = if (testing) "test@test.com" else System.getenv(NOTIFICATION_EMAIL)
    val password = if (testing) "123456" else System.getenv(NOTIFICATION_PASSWORD)
    appConfig.notificationConfig = NotificationConfig(email, password)
}