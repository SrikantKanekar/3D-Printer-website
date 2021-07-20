package com.example.features.checkout.presentation

import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.requests.CheckoutProceedRequest
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.checkoutProceed(checkoutRepository: CheckoutRepository) {
    post("/proceed") {

        val body = call.receive<CheckoutProceedRequest>()
        val (email) = call.principal<UserPrincipal>()!!

        val isEmpty = checkoutRepository.isCartEmpty(email)

        if (isEmpty) {
            when (body.success) {
                true -> {
                    val order = checkoutRepository.placeOrder(email)
                    call.respond(HttpStatusCode.Created, order)
                }
                false -> {
                    println("payment verification of $email is not successful")
                    call.respond(
                        HttpStatusCode.BadRequest,
                        "Payment is not successful"
                    )
                }
            }
        } else {
            call.respond(
                HttpStatusCode.MethodNotAllowed,
                "no checkout items"
            )
        }
    }
}