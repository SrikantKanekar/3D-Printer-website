package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.routes.accountGet
import com.example.features.checkout.presentation.updateAddress
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAccountRoute() {

    val accountRepository by inject<AccountRepository>()

    routing {
        route("/account"){
            authenticate(USER_AUTH) {
                accountGet(accountRepository)
                resetPassword(accountRepository)
                updateAccount(accountRepository)
                updateAddress(accountRepository)
            }
        }
    }
}

