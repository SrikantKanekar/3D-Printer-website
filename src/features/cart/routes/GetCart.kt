package com.example.features.cart.routes

import com.example.features.cart.data.CartRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getCart(cartRepository: CartRepository) {
    get("/cart") {
        val principal = call.principal<UserPrincipal>()!!
        val objects = cartRepository.getCartObjects(principal.email)
        call.respond(
            FreeMarkerContent(
                "cart.ftl",
                mapOf("objects" to objects, "user" to principal)
            )
        )
    }
}