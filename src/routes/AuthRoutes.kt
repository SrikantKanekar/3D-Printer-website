package com.example.routes

import com.example.data.models.UserIdPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.authRoutes(){

    route("/login") {
        authenticate("FORM_AUTH") {
            post {
                val principal = call.principal<UserIdPrincipal>()!!
                call.sessions.set(principal)
                call.respond(HttpStatusCode.OK, "OK")
            }
        }
    }
}