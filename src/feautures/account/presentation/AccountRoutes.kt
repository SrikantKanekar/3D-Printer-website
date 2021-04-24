package com.example.feautures.account.presentation

import com.example.feautures.auth.domain.UserIdPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.registerAccountRoutes(){
    routing {
        accountRoutes()
    }
}

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