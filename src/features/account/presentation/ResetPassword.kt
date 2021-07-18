package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.AccountConstants.INCORRECT_PASSWORD
import com.example.features.account.domain.AccountConstants.PASSWORD_DO_NOT_MATCH
import com.example.features.account.domain.AccountConstants.RESET_SUCCESSFUL
import com.example.features.account.domain.requests.ResetPasswordRequest
import com.example.features.auth.domain.UserPrincipal
import com.example.features.auth.domain.checkHashForPassword
import com.example.features.auth.domain.getHashWithSalt
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
