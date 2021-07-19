package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.objects.routes.getObjects
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.*
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

/**
 * This page will show the details about a object depending upon the ObjectStatus of the object.
 * 1) [NONE]/[CART] - user can edit object properties.
 * 2) [TRACKING]
 * 3) [COMPLETED]
 */
fun Application.registerObjectRoute() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        getCreateObjectRoute()
        postCreateObjectRoute(objectRepository)

        getObjects(objectRepository)
        getUpdateObjectRoute(objectRepository)
        slice(objectRepository)
        addToCart(objectRepository)
        removeFromCart(objectRepository)
        updateSetting(objectRepository)
        deleteObject(objectRepository)
    }
}

fun Route.getUpdateObjectRoute(objectRepository: ObjectRepository) {
    get("/object/{id}") {

        val id = call.parameters["id"]!!
        val principal = call.sessions.get<UserPrincipal>()

        val obj = when (principal) {
            null -> {
                val cookieObjects = call.sessions.get<ObjectsCookie>()?.objects
                cookieObjects?.find { it.id == id }
            }
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
