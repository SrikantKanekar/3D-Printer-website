package com.example.features.checkout.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.domain.Address
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateAddress(checkoutRepository: CheckoutRepository) {
    post("/checkout/address") {
        val parameters = call.receiveParameters()
        val firstname = parameters["firstname"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val lastname = parameters["lastname"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val phoneNumber = parameters["phoneNumber"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val add = parameters["address"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val city = parameters["city"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val state = parameters["state"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val country = parameters["country"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val pinCode = parameters["pinCode"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val address = Address(
            firstname = firstname,
            lastname = lastname,
            phoneNumber = phoneNumber.toLong(),
            address = add,
            city = city,
            state = state,
            country = country,
            pinCode = pinCode.toInt()
        )
        val principal = call.principal<UserPrincipal>()!!

        val updated = checkoutRepository.updateUserAddress(principal.email, address)
        call.respond(updated)
    }
}