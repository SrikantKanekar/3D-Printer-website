package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getObjectRoute(objectRepository: ObjectRepository) {
    get("/object/{id}") {

        val id = call.parameters["id"]!!
        val principal = call.sessions.get<UserPrincipal>()

        val obj = when (principal) {
            null -> call.sessions.get<ObjectsCookie>()?.objects?.find { it.id == id }
            else -> objectRepository.getUserObject(principal.email, id)
        }
        when (obj) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(
                FreeMarkerContent(
                    "object.ftl", mapOf(
                        "object" to obj,
                        "user" to (principal?.email ?: "")
                    )
                )
            )
        }
    }
}

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

fun Route.updateBasicSettings(objectRepository: ObjectRepository) {
    post("/object/{id}/basic") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val size = params["size"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val basicSettings = BasicSettings(size = size)

        val principal = call.sessions.get<UserPrincipal>()

        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.basicSettings = basicSettings
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateBasicSettings(principal.email, id, basicSettings)
        }
        call.respond(updated)
    }
}

fun Route.updateAdvancedSettings(objectRepository: ObjectRepository) {
    post("/object/{id}/advanced") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val weight = params["weight"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val advancedSettings = AdvancedSettings(weight = weight)

        val principal = call.sessions.get<UserPrincipal>()

        var updated = false
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                cookie.objects
                    .filter { it.status == NONE || it.status == CART }
                    .find { it.id == id }
                    ?.let {
                        it.advancedSettings = advancedSettings
                        updated = true
                    }
                call.sessions.set(cookie)
            }
            else -> updated =
                objectRepository.updateAdvancedSettings(principal.email, id, advancedSettings)
        }
        call.respond(updated)
    }
}
