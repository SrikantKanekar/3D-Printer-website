package com.example.features.cart.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.NONE
import com.example.util.enums.Quality
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectUpdateQuality(objectRepository: ObjectRepository) {
    put("/quality/{id}") {

        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val quality = call.receive<Quality>()
        val principal = call.principal<UserPrincipal>()
        var updated = false

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == CART || it.status == NONE }
                    .find { it.id == id }
                    ?.let {
                        it.quality = quality
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated = objectRepository.updateQuality(principal.email, id, quality)
        }

        when (updated) {
            true -> call.respond(quality)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Object quality of the given id is not changed"
            )
        }
    }
}