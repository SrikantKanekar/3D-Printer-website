package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectDelete(objectRepository: ObjectRepository) {
    delete("{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val principal = call.principal<UserPrincipal>()!!
        val deleted = objectRepository.deleteUserObject(principal.email, id)

        when (deleted) {
            true -> call.respond(HttpStatusCode.NoContent)
            false -> call.respond(
                HttpStatusCode.NotFound,
                "Object with the given id does not exist"
            )
        }
    }
}
