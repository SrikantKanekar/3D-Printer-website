package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.auth.domain.checkHashForPassword
import com.example.features.auth.domain.getHashWithSalt
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
        authenticate("SESSION_AUTH") {
            getAccountRoute(accountRepository)
            getUpdateAccountRoute(accountRepository)
            postUpdateAccountRoute(accountRepository)
            getResetPasswordRoute(accountRepository)
            postResetPasswordRoute(accountRepository)
            logoutRoute()
        }
    }
}

fun Route.getAccountRoute(accountRepository: AccountRepository) {
    get("/account") {
        val principal = call.principal<UserIdPrincipal>()!!
        val user = accountRepository.getUser(principal.email)
        call.respond(FreeMarkerContent("account.ftl", mapOf("user" to user)))
    }
}

private fun Route.getUpdateAccountRoute(accountRepository: AccountRepository) {
    get("/account/update") {
        val principal = call.principal<UserIdPrincipal>()!!
        val user = accountRepository.getUser(principal.email)
        call.respond(FreeMarkerContent("account_update.ftl", mapOf("user" to user)))
    }
}

fun Route.postUpdateAccountRoute(accountRepository: AccountRepository) {
    post("/account/update") {
        val principal = call.principal<UserIdPrincipal>()!!
        val params = call.receiveParameters()
        val username = params["username"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        try {
            val user = accountRepository.getUser(principal.email)
            val updated = accountRepository.updateUser(user.copy(username = username))
            if (updated) call.respondRedirect("/account")
        } catch (e: Exception) {
            print(e.localizedMessage)
            call.respond(HttpStatusCode.NotFound, "Account doesn't exist")
        }
    }
}

private fun Route.getResetPasswordRoute(accountRepository: AccountRepository) {
    get("/account/reset-password") {
        val principal = call.principal<UserIdPrincipal>()!!
        val user = accountRepository.getUser(principal.email)
        call.respond(FreeMarkerContent("account_reset_password.ftl", mapOf("user" to user)))
    }
}

private fun Route.postResetPasswordRoute(accountRepository: AccountRepository) {
    post("/account/reset-password") {
        val principal = call.principal<UserIdPrincipal>()!!
        val params = call.receiveParameters()
        val oldPassword = params["old_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val newPassword = params["new_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val confirmPassword = params["confirm_password"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        try {
            if (newPassword == confirmPassword) {
                val user = accountRepository.getUser(principal.email)
                val isPasswordCorrect = checkHashForPassword(oldPassword, user.password)

                if (isPasswordCorrect) {
                    val updated = accountRepository.updateUser(
                        user.copy(password = getHashWithSalt(newPassword))
                    )
                    if (updated) call.respondRedirect("/account")
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "password not correct")
                }
            } else {
                call.respond(HttpStatusCode.NotAcceptable, "password's don't match")
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.NotFound, "Account doesn't exist")
        }
    }
}

private fun Route.logoutRoute() {
    get("/account/logout") {
        call.sessions.clear<UserIdPrincipal>()
        call.respondRedirect("/")
    }
}
