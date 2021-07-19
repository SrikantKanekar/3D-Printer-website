package com.example.features.`object`.presentation

import com.example.features.cart.data.CartRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.cartAdd(objectRepository: CartRepository) {
    post("/object/add-to-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> call.respondText("/auth/login?returnUrl=/object/$id")
            else -> {
                val result = objectRepository.addToCart(principal.email, id)
                call.respond(result.toString())
            }
        }
    }
}
