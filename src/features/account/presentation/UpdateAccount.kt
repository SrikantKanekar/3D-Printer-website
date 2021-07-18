package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.requests.UpdateAccountRequest
import com.example.features.auth.domain.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateAccountRoute(accountRepository: AccountRepository) {
    post("/account/update") {
        val body = call.receive<UpdateAccountRequest>()
        val principal = call.principal<UserPrincipal>()!!
        accountRepository.updateUsername(principal.email, body.username)
        call.respond(body)
    }
}