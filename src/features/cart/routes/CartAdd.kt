package com.example.features.`object`.presentation

import com.example.features.cart.data.CartRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.cartAdd(objectRepository: CartRepository) {
    post("{id}") {
        val id = call.parameters["id"] ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val principal = call.principal<UserPrincipal>()!!
        val result = objectRepository.addToCart(principal.email, id)

        when (result) {
            true -> call.respond(HttpStatusCode.NoContent)
            false -> call.respond(HttpStatusCode.MethodNotAllowed)
        }
    }
}
