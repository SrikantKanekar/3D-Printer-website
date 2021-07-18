package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.requests.ResetPasswordRequest
import com.example.model.UserPrincipal
import com.example.util.checkHashForPassword
import com.example.util.constants.Account.INCORRECT_PASSWORD
import com.example.util.constants.Account.PASSWORD_DO_NOT_MATCH
import com.example.util.constants.Account.RESET_SUCCESSFUL
import com.example.util.getHashWithSalt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.resetPasswordRoute(accountRepository: AccountRepository) {
    post("/account/reset-password") {
        val (oldPassword, newPassword, confirmPassword) = call.receive<ResetPasswordRequest>()

        if (newPassword == confirmPassword) {
            val principal = call.principal<UserPrincipal>()!!
            val user = accountRepository.getUser(principal.email)
            val isPasswordCorrect = checkHashForPassword(oldPassword, user.password)

            if (isPasswordCorrect) {
                user.password = getHashWithSalt(newPassword)
                accountRepository.updateUser(user)
                call.respondText(RESET_SUCCESSFUL)
            } else {
                call.respondText(INCORRECT_PASSWORD)
            }
        } else {
            call.respondText(PASSWORD_DO_NOT_MATCH)
        }
    }
}
