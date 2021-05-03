package com.example.features.cart.presentation

import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.cart.data.CartRepository
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
        authenticate("SESSION_AUTH") {
            getCartRoute(cartRepository)
            removeFromCart(cartRepository)
        }
    }
}

private fun Route.getCartRoute(cartRepository: CartRepository) {
    get("/cart") {
        val principal = call.principal<UserIdPrincipal>()!!
        val orders = cartRepository.getUserCartOrders(principal.email)
        call.respond(FreeMarkerContent("cart.ftl", mapOf("orders" to orders, "user" to principal)))
    }
}

private fun Route.removeFromCart(cartRepository: CartRepository) {
    get("/cart/{id}/remove") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.principal<UserIdPrincipal>()!!
        val result = cartRepository.removeCartOrder(principal.email, id)

        if (result) {
            call.respondRedirect("/cart")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
        }
    }
}
