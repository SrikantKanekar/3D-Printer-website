package com.example.features.objects.routes

import com.example.features.`object`.data.ObjectRepository
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getObjects(objectsRepository: ObjectRepository) {
    get("/objects") {

        val principal = call.principal<UserPrincipal>()
        val objs = if (principal != null) {
            objectsRepository.getUserObjects(principal.email)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            listOf(cookie.objects.reversed())
        }
        call.respond(objs)
    }
}