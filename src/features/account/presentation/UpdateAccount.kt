package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.auth.domain.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateAccountRoute(accountRepository: AccountRepository) {
    post("/account/update") {
        val params = call.receiveParameters()
        val username = params["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.principal<UserPrincipal>()!!
        val updated = accountRepository.updateUsername(principal.email, username)
        if (updated) {
            call.respondText("updated")
        } else {
            call.respondText("error")
        }
    }
}