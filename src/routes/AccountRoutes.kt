package com.example.routes

import com.example.data.models.UserIdPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.accountRoutes(){

    route("/user_info") {
        authenticate("SESSION_AUTH") {
            get {
                val principal = call.principal<UserIdPrincipal>()!!
                call.respond(HttpStatusCode.OK, principal)
            }
        }
    }
}