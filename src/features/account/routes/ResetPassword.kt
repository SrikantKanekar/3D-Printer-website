package com.example.features.account.presentation

import com.example.features.account.data.AccountRepository
import com.example.features.account.domain.requests.ResetPasswordRequest
import com.example.model.UserPrincipal
import com.example.util.checkPassword
import com.example.util.constants.Account.INCORRECT_PASSWORD
import com.example.util.constants.Account.PASSWORD_DO_NOT_MATCH
import com.example.util.generateHash
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.resetPassword(accountRepository: AccountRepository) {
    put("/reset-password") {
        val (oldPassword, newPassword, confirmPassword) = call.receive<ResetPasswordRequest>()

        if (newPassword == confirmPassword) {
            val principal = call.principal<UserPrincipal>()!!
            val user = accountRepository.getUser(principal.email)
            val isPasswordCorrect = checkPassword(oldPassword, user.password)

            if (isPasswordCorrect) {
                user.password = generateHash(newPassword)
                accountRepository.updateUser(user)
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.BadRequest, INCORRECT_PASSWORD)
            }
        } else {
            call.respond(HttpStatusCode.BadRequest, PASSWORD_DO_NOT_MATCH)
        }
    }
}
