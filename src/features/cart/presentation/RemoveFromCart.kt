package com.example.features.cart.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.cart.data.CartRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.removeFromCart(cartRepository: CartRepository) {
    post("/cart/remove") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.removeCartObject(principal.email, id)
        call.respond(result)
    }
}
