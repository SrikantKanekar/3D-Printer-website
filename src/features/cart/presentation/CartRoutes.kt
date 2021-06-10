package com.example.features.cart.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.cart.data.CartRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCartRoute() {

    val cartRepository by inject<CartRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getCartRoute(cartRepository)
            removeFromCart(cartRepository)
            clearCart(cartRepository)
            updateQuantity(cartRepository)
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
    post("/cart/remove") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.removeCartObject(principal.email, id)
        call.respond(result)
    }
}

private fun Route.clearCart(cartRepository: CartRepository) {
    post("/cart/clear") {
        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.clearCart(principal.email)
        call.respond(result)
    }
}

private fun Route.updateQuantity(cartRepository: CartRepository) {
    post("/cart/quantity") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val quantity = params["quantity"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.principal<UserPrincipal>()!!
        val result = cartRepository.updateQuantity(principal.email, id, quantity)
        call.respond(result)
    }
}
