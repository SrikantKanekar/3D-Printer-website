package com.example.features.auth.presentation

import com.example.features.wishlist.domain.WishlistCookie
import com.example.features.account.domain.User
import com.example.features.auth.data.AuthRepository
import com.example.features.auth.domain.Constants.EMAIL_ALREADY_TAKEN
import com.example.features.auth.domain.Constants.EMAIL_PASSWORD_INCORRECT
import com.example.features.auth.domain.Constants.UNKNOWN_REGISTRATION_ERROR
import com.example.features.auth.domain.Login
import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.auth.domain.getHashWithSalt
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
        val returnUrl = call.parameters["returnUrl"] ?: "/"
        call.respond(FreeMarkerContent("auth_login.ftl", mapOf("returnUrl" to returnUrl)))
    }
}

fun Route.postLoginRoute(authRepository: AuthRepository) {
    post("/auth/login") {
        val params = call.receiveParameters()
        val email = params["Email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val returnUrl = params["returnUrl"] ?: "/"

        val isPasswordCorrect = authRepository.login(email, password)
        if (isPasswordCorrect) {
            call.sessions.set(UserIdPrincipal(email))
            val cookie = call.sessions.get<WishlistCookie>()
            authRepository.syncOrders(email, cookie)
            call.sessions.clear<WishlistCookie>()
            call.respondText(returnUrl)
        } else {
            call.respondText(EMAIL_PASSWORD_INCORRECT)
        }
    }
}

fun Route.getRegisterRoute() {
    get("auth/register") {
        val returnUrl = call.parameters["returnUrl"] ?: "/"
        call.respond(FreeMarkerContent("auth_register.ftl", mapOf("returnUrl" to returnUrl)))
    }
}

fun Route.postRegisterRoute(authRepository: AuthRepository) {
    post("auth/register") {
        val params = call.receiveParameters()
        val email = params["Email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val username = params["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val returnUrl = params["returnUrl"] ?: "/"

        val userExists = authRepository.checkIfUserExists(email)
        if (!userExists) {
            if (authRepository.register(User(email, getHashWithSalt(password), username))) {
                call.sessions.set(UserIdPrincipal(email))
                val cookie = call.sessions.get<WishlistCookie>()
                authRepository.syncOrders(email, cookie)
                call.sessions.clear<WishlistCookie>()
                call.respondText(returnUrl)
            } else {
                call.respondText(UNKNOWN_REGISTRATION_ERROR)
            }
        } else {
            call.respondText(EMAIL_ALREADY_TAKEN)
        }
    }
}

private fun Route.loginProvider() {
    authenticate("OAuth") {
        location<Login> {
            println("-------------aaaa------------")
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

