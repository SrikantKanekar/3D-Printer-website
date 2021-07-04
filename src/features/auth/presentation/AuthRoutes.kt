package com.example.features.auth.presentation

import com.example.features.auth.data.AuthRepository
import io.ktor.application.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAuthRoutes() {
    val authRepository by inject<AuthRepository>()

    routing {
        getLoginRoute()
        postLoginRoute(authRepository)
        getRegisterRoute()
        postRegisterRoute(authRepository)
        loginProvider()
    }
}