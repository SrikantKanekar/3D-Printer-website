package com.example.config

import com.example.util.JWT_SECRET
import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    //JWT
    val jwt = environment.config.config("ktor.jwt")
    val secret = System.getenv(JWT_SECRET)
    val issuer = jwt.property("issuer").getString()
    val audience = jwt.property("audience").getString()
    val realm = jwt.property("realm").getString()

    appConfig.jwtConfig = JWTConfig(secret, issuer, audience, realm)
}