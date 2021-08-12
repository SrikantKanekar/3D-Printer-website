package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.SlicingDetails
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.NONE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectSlice(objectRepository: ObjectRepository) {
    put("/slice/{id}") {

        val body = call.receive<SlicingDetails>()
        body.validate()

        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val principal = call.principal<UserPrincipal>()
        var result = false

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE && it.setting.updated }
                    .find { it.id == id }
                    ?.apply {
                        slicingDetails = body
                        setting.updated = false
                        result = true
                    }
                call.sessions.set(cookie)
            }
            else -> {
                result = objectRepository.sliceUserObject(principal.email, id, body)
            }
        }
        if (result) {
            call.respond(body)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}