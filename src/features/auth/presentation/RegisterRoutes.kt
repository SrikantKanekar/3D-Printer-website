package com.example.features.auth.presentation

import com.example.features.account.domain.User
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.AuthConstants.EMAIL_ALREADY_TAKEN
import com.example.features.auth.domain.AuthConstants.PASSWORDS_DO_NOT_MATCH
import com.example.features.auth.domain.AuthConstants.UNKNOWN_REGISTRATION_ERROR
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.getHashWithSalt
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getRegisterRoute() {
    get("auth/register") {
        val principal = call.sessions.get<UserPrincipal>()
        if (principal != null) {
            call.respondRedirect("/account")
        } else {
            val returnUrl = call.parameters["returnUrl"] ?: "/"
            call.respond(FreeMarkerContent("auth_register.ftl", mapOf("returnUrl" to returnUrl)))
        }
    }
}

fun Route.postRegisterRoute(authRepository: AuthRepository) {
    post("auth/register") {
        val params = call.receiveParameters()
        val username = params["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password1 = params["password1"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password2 = params["password2"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val returnUrl = params["returnUrl"] ?: "/"

        if (password1 == password2) {
            val userExists = authRepository.doesUserExist(email)
            if (!userExists) {
                val newUser = User(email, getHashWithSalt(password1), username)
                val registered = authRepository.register(newUser)
                if (registered) {
                    call.sessions.set(UserPrincipal(email))
                    val cookie = call.sessions.get<ObjectsCookie>()
                    authRepository.syncCookieObjects(email, cookie)
                    call.sessions.clear<ObjectsCookie>()
                    call.respondText(returnUrl)
                } else {
                    call.respondText(UNKNOWN_REGISTRATION_ERROR)
                }
            } else {
                call.respondText(EMAIL_ALREADY_TAKEN)
            }
        } else {
            call.respondText(PASSWORDS_DO_NOT_MATCH)
        }
    }
}