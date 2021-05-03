package com.example.features.util.presentation

import com.example.features.auth.domain.UserIdPrincipal
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Application.registerHomeRoute() {

    routing {
        homeRoute()
    }
}

fun Route.homeRoute() {
    get("/") {
        val principal = call.sessions.get<UserIdPrincipal>()
        call.respond(FreeMarkerContent("home.ftl", mapOf("user" to (principal?.email ?: ""))))
    }
}