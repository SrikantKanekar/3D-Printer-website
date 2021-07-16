package com.example.config

import io.ktor.application.*
import org.koin.ktor.ext.inject

fun Application.setupConfig() {
    val appConfig by inject<AppConfig>()

    //JWT
    val jwt = environment.config.config("ktor.jwt")
    val secret = jwt.property("secret").getString()
    val issuer = jwt.property("issuer").getString()
    val audience = jwt.property("audience").getString()
    val realm = jwt.property("realm").getString()

    appConfig.jwtConfig = JWTConfig(secret, issuer, audience, realm)
}