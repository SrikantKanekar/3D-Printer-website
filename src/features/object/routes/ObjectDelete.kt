package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus
import com.example.util.enums.ObjectStatus.NONE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectDelete(objectRepository: ObjectRepository) {
    delete("{id}") {
        val id = call.parameters["id"] ?: return@delete call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val principal = call.principal<UserPrincipal>()
        val deleted: Boolean

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                deleted = cookie.objects
                    .removeIf { it.id == id && (it.status == NONE || it.status == ObjectStatus.CART) }
                call.sessions.set(cookie)
            }
            else -> {
                deleted = objectRepository.deleteUserObject(principal.email, id)
            }
        }

        when (deleted) {
            true -> call.respond(HttpStatusCode.NoContent)
            false -> call.respond(
                HttpStatusCode.NotFound,
                "Object with the given id does not exist"
            )
        }
    }
}
