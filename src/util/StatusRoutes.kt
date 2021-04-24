package com.example.util

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import javax.naming.AuthenticationException

fun Application.registerStatusRoutes(){
    routing {
        statusRoutes()
    }
}

fun Route.statusRoutes(){

    install(StatusPages) {
        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized)
        }
    }
}