package com.example.feautures.util.presentation

import com.example.feautures.auth.data.AuthRepository
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerHomeRoute() {
    val authRepository by inject<AuthRepository>()

    routing {
        homeRoute(authRepository)
    }
}

fun Route.homeRoute(authRepository: AuthRepository) {
    get("/") {
        call.respond(FreeMarkerContent("home.ftl", null))
    }
}