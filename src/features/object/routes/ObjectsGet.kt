package com.example.features.objects.routes

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.objectsGet(objectsRepository: ObjectRepository) {
    get {

        val principal = call.principal<UserPrincipal>()

        val objs = when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects.reversed()
            }
            else -> objectsRepository.getObjectsByUser(principal.email)
        }
        call.respond(objs)
    }
}