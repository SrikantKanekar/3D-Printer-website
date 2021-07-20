package com.example.features.checkout.presentation

import com.example.features.account.data.AccountRepository
import com.example.model.Address
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateAddress(accountRepository: AccountRepository) {
    post("/address") {
        val body = call.receive<Address>()
        body.validate()

        val principal = call.principal<UserPrincipal>()!!
        accountRepository.updateAddress(principal.email, body)
        call.respond(body)
    }
}