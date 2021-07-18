package com.example.features.checkout.presentation

import com.example.model.UserPrincipal
import com.example.features.checkout.data.CheckoutRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.paymentSucceed(checkoutRepository: CheckoutRepository) {
    post("/checkout/success") {

        val parameters = call.receiveParameters()
        val success = parameters["success"]?.toBoolean() ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (success){
            val principal = call.principal<UserPrincipal>()!!
            val result = checkoutRepository.placeOrder(principal.email)
            call.respond(result)
        } else {
            call.respondText("Order is not placed")
        }
    }
}