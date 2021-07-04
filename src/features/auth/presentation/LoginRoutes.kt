package com.example.features.auth.presentation

import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.AuthConstants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getLoginRoute() {
    get("/auth/login") {
        val principal = call.sessions.get<UserPrincipal>()
        if (principal != null) {
            call.respondRedirect("/account")
        } else {
            val returnUrl = call.parameters["returnUrl"] ?: "/"
            call.respond(FreeMarkerContent("auth_login.ftl", mapOf("returnUrl" to returnUrl)))
        }
    }
}

fun Route.postLoginRoute(authRepository: AuthRepository) {
    post("/auth/login") {
        val params = call.receiveParameters()
        val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val returnUrl = params["returnUrl"] ?: "/"

        val isPasswordCorrect = authRepository.login(email, password)
        if (isPasswordCorrect) {
            call.sessions.set(UserPrincipal(email))
            val cookie = call.sessions.get<ObjectsCookie>()
            authRepository.syncCookieObjects(email, cookie)
            call.sessions.clear<ObjectsCookie>()
            call.respondText(returnUrl)
        } else {
            call.respondText(EMAIL_PASSWORD_INCORRECT)
        }
    }
}