package com.example.features.auth.presentation

import com.example.config.JWTConfig
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.LoginRequest
import com.example.model.ObjectsCookie
import com.example.setUp.generateJwtToken
import com.example.util.constants.Auth.EMAIL_PASSWORD_INCORRECT
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.loginRoute(authRepository: AuthRepository, jwt: JWTConfig) {
    post("/auth/login") {
        val (email, password) = call.receive<LoginRequest>()

        val isPasswordCorrect = authRepository.login(email, password)
        if (isPasswordCorrect) {

            val cookie = call.sessions.get<ObjectsCookie>()
            authRepository.syncCookieObjects(email, cookie)
            call.sessions.clear<ObjectsCookie>()

            val user = authRepository.getUser(email)
            val token = generateJwtToken(jwt, user)
            call.respond(token)
        } else {
            call.respond(HttpStatusCode.BadRequest, EMAIL_PASSWORD_INCORRECT)
        }
    }
}