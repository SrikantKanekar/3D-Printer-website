package com.example.features.cart.routes

import com.example.features.cart.data.CartRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.cartGet(cartRepository: CartRepository) {
    get {
        val principal = call.principal<UserPrincipal>()!!
        val objects = cartRepository.getCartObjects(principal.email)
        call.respond(objects)
    }
}