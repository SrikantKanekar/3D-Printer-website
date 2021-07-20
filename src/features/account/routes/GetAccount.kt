package com.example.features.account.routes

import com.example.features.account.data.AccountRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.accountGet(accountRepository: AccountRepository) {
    get {
        val principal = call.principal<UserPrincipal>()!!
        val user = accountRepository.getUser(principal.email)
        user.password = ""
        call.respond(user)
    }
}
