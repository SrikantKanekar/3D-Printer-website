package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.routes.getAccountDetails
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
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

