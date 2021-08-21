package com.example.features.`object`.routes

import com.example.features.`object`.data.ObjectRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectGet(objectRepository: ObjectRepository) {
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val principal = call.principal<UserPrincipal>()!!

        val obj = objectRepository.getUserObjectById(principal.email, id)

        when (obj) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(obj)
        }
    }
}
