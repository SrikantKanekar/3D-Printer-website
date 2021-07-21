package com.example.features.auth.presentation

import com.example.config.AppConfig
import com.example.features.account.presentation.resetPassword
import com.example.features.auth.data.AuthRepository
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAuthRoutes() {

    val authRepository by inject<AuthRepository>()
    val appConfig by inject<AppConfig>()

    routing {
        route("/auth"){
            loginRoute(authRepository, appConfig.jwtConfig)
            registerRoute(authRepository, appConfig.jwtConfig)

            authenticate(USER_AUTH){
                resetPassword(authRepository)
            }
        }
    }
}