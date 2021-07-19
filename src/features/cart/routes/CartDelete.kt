package com.example.features.cart.presentation

import com.example.features.cart.data.CartRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.cartDelete(cartRepository: CartRepository) {
    post("/cart/remove") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.removeCartObject(principal.email, id)
        call.respond(result)
    }
}
