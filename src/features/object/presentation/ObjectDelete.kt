package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.ObjectStatus.NONE
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.deleteObject(objectRepository: ObjectRepository) {
    post("/object/delete") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val deleted: Boolean
        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                deleted = cookie.objects.removeIf { it.id == id && it.status == NONE }
                call.sessions.set(cookie)
            }
            else -> {
                deleted = objectRepository.deleteUserObject(principal.email, id)
            }
        }
        call.respond(deleted)
    }
}
