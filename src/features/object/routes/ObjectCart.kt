package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.addToCart(objectRepository: ObjectRepository) {
    post("/object/add-to-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> call.respondText("/auth/login?returnUrl=/object/$id")
            else -> {
                val result = objectRepository.addToCart(principal.email, id)
                call.respond(result.toString())
            }
        }
    }
}

fun Route.removeFromCart(objectRepository: ObjectRepository) {
    post("/object/remove-from-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()!!
        val result = objectRepository.removeFromCart(principal.email, id)
        call.respond(result)
    }
}
