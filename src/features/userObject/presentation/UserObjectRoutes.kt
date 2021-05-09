package com.example.features.userObject.presentation

import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.data.UserObjectRepository
import com.example.features.userObject.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerMyObjectsRoutes() {

    val userObjectRepository by inject<UserObjectRepository>()

    routing {
        getUserObjectRoute(userObjectRepository)
        deleteUserObject(userObjectRepository)
        addToCartRoute(userObjectRepository)
    }
}

fun Route.getUserObjectRoute(userObjectRepository: UserObjectRepository) {
    get("/wishlist") {

        val principal = call.sessions.get<UserPrincipal>()
        val objs = if (principal != null) {
            userObjectRepository.getUserObjects(principal.email)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            ArrayList(cookie.objects.filter { it.status == NONE })
        }
        call.respond(
            FreeMarkerContent(
                "wishlist.ftl",
                mapOf(
                    "orders" to objs,
                    "user" to (principal?.email ?: "")
                )
            )
        )
    }
}

private fun Route.deleteUserObject(userObjectRepository: UserObjectRepository) {
    get("/wishlist/{id}/delete") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        var serverResult = false

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                val deleted = cookie.objects.removeIf { it.id == id && it.status == NONE }
                call.sessions.set(cookie)
                if (deleted) serverResult = userObjectRepository.deleteServerObjectFile(id)
            }
            else -> {
                val deleted = userObjectRepository.deleteUserObject(principal.email, id)
                if (deleted) serverResult = userObjectRepository.deleteServerObjectFile(id)
            }
        }
        if (serverResult) {
            call.respondRedirect("/wishlist")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
        }
    }
}

private fun Route.addToCartRoute(userObjectRepository: UserObjectRepository) {
    get("/wishlist/{id}/cart") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> {
                call.respondRedirect {
                    path("auth/login")
                    parameters.append("returnUrl", call.request.uri)
                }
            }
            else -> {
                val result = userObjectRepository.addToCart(principal.email, id)
                if (result) {
                    call.respondRedirect("/wishlist")
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
                }
            }
        }
    }
}
