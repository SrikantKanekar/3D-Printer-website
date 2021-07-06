package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.auth.domain.UserPrincipal
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerAccountRoute() {

    val accountRepository by inject<AccountRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getAccountRoute(accountRepository)
            logoutRoute()

            resetPasswordRoute(accountRepository)
            updateAccountRoute(accountRepository)
        }
    }
}

fun Route.getAccountRoute(accountRepository: AccountRepository) {
    get("/account") {
        val principal = call.principal<UserPrincipal>()!!
        val user = accountRepository.getUser(principal.email)
        call.respond(FreeMarkerContent("account.ftl", mapOf("user" to user)))
    }
}

private fun Route.logoutRoute() {
    get("/account/logout") {
        call.sessions.clear<UserPrincipal>()
        call.respondRedirect("/")
    }
}
