package com.example.features.cart.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.UserPrincipal
import com.example.util.enums.Quality
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectUpdateQuality(objectRepository: ObjectRepository) {
    put("/quality/{id}") {

        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val quality = call.receive<Quality>()
        val principal = call.principal<UserPrincipal>()!!
        val updated = objectRepository.updateQuality(principal.email, id, quality)

        when (updated) {
            true -> call.respond(quality)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Object quality of the given id is not changed"
            )
        }
    }
}