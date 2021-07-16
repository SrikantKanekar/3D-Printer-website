package com.example.features.util.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.util.AUTH.JWT_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.registerIndexRoute() {

    routing {
        authenticate(JWT_AUTH) {
            getIndexRoute()
        }
    }
}

fun Route.getIndexRoute() {
    get("/") {
        val principal = call.sessions.get<UserPrincipal>()
        call.respond(FreeMarkerContent("index.ftl", mapOf("user" to (principal?.email ?: ""))))
    }
}