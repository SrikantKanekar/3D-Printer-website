package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.Setting
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.NONE
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectUpdateSetting(objectRepository: ObjectRepository) {
    put("/setting/{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val body = call.receive<Setting>()
        val principal = call.principal<UserPrincipal>()
        var updated = false

        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE }
                    .find { it.id == id }
                    ?.let {
                        it.setting = body
                        it.slicingDetails.uptoDate = false
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateSetting(principal.email, id, body)
        }
        when (updated) {
            true -> call.respond(body)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Object setting of the given id is not changed"
            )
        }
    }
}
