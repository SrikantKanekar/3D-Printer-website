package com.example.features.auth.presentation

import com.example.features.userObject.domain.ObjectsCookie
import com.example.features.account.domain.User
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.Constants.EMAIL_ALREADY_TAKEN
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.Constants.PASSWORDS_DO_NOT_MATCH
import com.example.features.auth.domain.Constants.UNKNOWN_REGISTRATION_ERROR
import com.example.features.auth.domain.Login
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.getHashWithSalt
import com.example.util.AUTH.OAUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
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

private fun Route.loginProvider() {
    authenticate(OAUTH) {
        location<Login> {
            param("error") {
                handle {
                    call.respond(HttpStatusCode.BadRequest, call.parameters.getAll("error").orEmpty())
                }
            }

            handle {
                val principal = call.authentication.principal<OAuthAccessTokenResponse.OAuth2>()
                if (principal != null) {

                    println("-----------------------------")
                    println(principal.accessToken)
                    println(principal.expiresIn)
                    println(principal.extraParameters)
                    println(principal.refreshToken)
                    println(principal.tokenType)
                    call.respondText("Success")
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "No account received")
                }
            }
        }
    }
}

