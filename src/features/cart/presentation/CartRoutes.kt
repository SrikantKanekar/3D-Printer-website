package com.example.features.cart.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.cart.data.CartRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCartRoutes() {

    val cartRepository by inject<CartRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getCartRoute(cartRepository)
            removeFromCart(cartRepository)
        }
    }
}

private fun Route.getCartRoute(cartRepository: CartRepository) {
    get("/cart") {
        val principal = call.principal<UserPrincipal>()!!
        val obj = cartRepository.getUserCartObjects(principal.email)
        call.respond(
            FreeMarkerContent(
                "cart.ftl",
                mapOf("objects" to obj, "user" to principal)
            )
        )
    }
}

private fun Route.removeFromCart(cartRepository: CartRepository) {
    get("/cart/{id}/remove") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.removeCartObject(principal.email, id)
        if (result) {
            call.respondRedirect("/cart")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid object ID")
        }
    }
}
