package com.example.features.checkout.routes

import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.response.CheckoutResponse
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.checkoutGet(checkoutRepository: CheckoutRepository) {
    get {
        val principal = call.principal<UserPrincipal>()!!
        val objects = checkoutRepository.getCartObjects(principal.email)
        val address = checkoutRepository.getUserAddress(principal.email)
        call.respond(CheckoutResponse(objects, address))
    }
}