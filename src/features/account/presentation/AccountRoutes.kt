package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.Constants.INCORRECT_PASSWORD
import com.example.features.account.domain.Constants.PASSWORD_DO_NOT_MATCH
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.checkHashForPassword
import com.example.features.auth.domain.getHashWithSalt
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerAccountRoutes() {

    val accountRepository by inject<AccountRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getAccountRoute(accountRepository)
            updateAccountRoute(accountRepository)
            resetPasswordRoute(accountRepository)
            logoutRoute()
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

private fun Route.resetPasswordRoute(accountRepository: AccountRepository) {
    post("/account/reset-password") {
        val params = call.receiveParameters()
        val oldPassword = params["old_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val newPassword = params["new_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val confirmPassword = params["confirm_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (newPassword == confirmPassword) {
            val principal = call.principal<UserPrincipal>()!!
            val user = accountRepository.getUser(principal.email)
            val isPasswordCorrect = checkHashForPassword(oldPassword, user.password)

            if (isPasswordCorrect) {
                val updated = accountRepository.updateUser(
                    user.copy(password = getHashWithSalt(newPassword))
                )
                if (updated) {
                    call.respondText("updated")
                } else {
                    call.respondText("error")
                }
            } else {
                call.respondText(INCORRECT_PASSWORD)
            }
        } else {
            call.respondText(PASSWORD_DO_NOT_MATCH)
        }
    }
}

private fun Route.logoutRoute() {
    get("/account/logout") {
        call.sessions.clear<UserPrincipal>()
        call.respondRedirect("/")
    }
}
