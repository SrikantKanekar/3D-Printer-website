package com.example.features.auth.presentation

import com.example.features.auth.domain.Login
import com.example.util.AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.loginProvider() {
    authenticate(AUTH.OAUTH) {
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