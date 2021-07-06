package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.AccountConstants
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.checkHashForPassword
import com.example.features.auth.domain.getHashWithSalt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.resetPasswordRoute(accountRepository: AccountRepository) {
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
                call.respondText(AccountConstants.INCORRECT_PASSWORD)
            }
        } else {
            call.respondText(AccountConstants.PASSWORD_DO_NOT_MATCH)
        }
    }
}
