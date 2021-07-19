package com.example.features.account.routes

import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAccountDetails() {
    get("/account") {
        val principal = call.principal<UserPrincipal>()!!
        call.respond(principal)
    }
}
