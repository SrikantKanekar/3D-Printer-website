package com.example.features.myObjects.presentation

import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.myObjects.data.MyObjectsRepository
import com.example.features.myObjects.domain.ObjectsCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerMyObjectsRoutes() {

    val wishlistRepository by inject<MyObjectsRepository>()

    routing {
        getWishlistRoute(wishlistRepository)
        deleteFromWishlistRoute(wishlistRepository)
        addToCartRoute(wishlistRepository)
    }
}

fun Route.getWishlistRoute(myObjectsRepository: MyObjectsRepository) {
    get("/wishlist") {

        val principal = call.sessions.get<UserIdPrincipal>()
        val objs = if (principal != null) {
            myObjectsRepository.getMyObjects(principal.email)
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

private fun Route.deleteFromWishlistRoute(myObjectsRepository: MyObjectsRepository) {
    get("/wishlist/{id}/delete") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val serverResult: Boolean
        val userResult: Boolean

        val principal = call.sessions.get<UserIdPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                userResult = cookie.objects.removeIf { it.id == id }
                serverResult = myObjectsRepository.deleteServerObjectFile(id)
                call.sessions.set(cookie)
            }
            else -> {
                serverResult = myObjectsRepository.deleteServerObjectFile(id)
                userResult = myObjectsRepository.deleteUserObject(principal.email, id)
            }
        }
        if (userResult and serverResult) {
            call.respondRedirect("/wishlist")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
        }
    }
}

private fun Route.addToCartRoute(myObjectsRepository: MyObjectsRepository) {
    get("/wishlist/{id}/cart") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.sessions.get<UserIdPrincipal>()
        when (principal) {
            null -> {
                call.respondRedirect {
                    path("auth/login")
                    parameters.append("returnUrl", call.request.uri)
                }
            }
            else -> {
                val result = myObjectsRepository.addToCart(principal.email, id)
                if (result) {
                    call.respondRedirect("/wishlist")
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
                }
            }
        }
    }
}
