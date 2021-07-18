package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.util.AUTH.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAccountRoute() {

    val accountRepository by inject<AccountRepository>()

    routing {
        authenticate(USER_AUTH) {
            getAccountDetails()
            resetPasswordRoute(accountRepository)
            updateAccountRoute(accountRepository)
        }
    }
}

fun Route.getAccountDetails() {
    get("/account") {
        val principal = call.principal<UserPrincipal>()!!
        call.respond(principal)
    }
}
