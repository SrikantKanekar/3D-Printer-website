package com.example.features.cart.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.requests.ObjectQuantityRequest
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.NONE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectUpdateQuantity(objectRepository: ObjectRepository) {
    put("/quantity/{id}") {

        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val (quantity) = call.receive<ObjectQuantityRequest>()
        val principal = call.principal<UserPrincipal>()
        var updated = false

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == CART || it.status == NONE }
                    .find { it.id == id }
                    ?.let {
                        it.quantity = quantity
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated = objectRepository.updateQuantity(principal.email, id, quantity)
        }

        when (updated) {
            true -> call.respond(quantity)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Object quantity of the given id is not changed"
            )
        }
    }
}