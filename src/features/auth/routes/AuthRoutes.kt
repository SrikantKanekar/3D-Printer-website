package com.example.features.auth.presentation

import com.example.config.AppConfig
import com.example.features.auth.data.AuthRepository
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAuthRoutes() {

    val authRepository by inject<AuthRepository>()
    val appConfig by inject<AppConfig>()

    routing {
        loginRoute(authRepository, appConfig.jwtConfig)
        registerRoute(authRepository, appConfig.jwtConfig)
    }
}