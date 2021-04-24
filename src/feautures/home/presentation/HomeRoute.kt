package com.example.feautures.home.presentation

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
        call.respond(FreeMarkerContent("index.ftl", mapOf("users" to authRepository.getAllUsers())))
    }
}