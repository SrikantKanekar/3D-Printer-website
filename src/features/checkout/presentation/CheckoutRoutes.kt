package com.example.features.checkout.presentation

import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.domain.Address
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCheckoutRoutes() {

    val checkoutRepository by inject<CheckoutRepository>()

    routing {
        authenticate("SESSION_AUTH") {
            getCheckoutRoute(checkoutRepository)
            removeFromCheckout(checkoutRepository)
            proceedToPay(checkoutRepository)
        }
    }
}

private fun Route.getCheckoutRoute(checkoutRepository: CheckoutRepository) {
    get("/checkout") {
        val principal = call.principal<UserIdPrincipal>()!!
        val address = checkoutRepository.getUserAddress(principal.email)
        val orders = checkoutRepository.getUserCartOrders(principal.email)
        call.respond(
            FreeMarkerContent(
                "checkout.ftl", mapOf(
                    "orders" to orders,
                    "user" to principal,
                    "address" to address
                )
            )
        )
    }
}

private fun Route.removeFromCheckout(checkoutRepository: CheckoutRepository) {
    get("/checkout/{id}/remove") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.principal<UserIdPrincipal>()!!
        val result = checkoutRepository.removeCartOrder(principal.email, id)

        if (result) {
            call.respondRedirect("/checkout")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
        }
    }
}

private fun Route.proceedToPay(checkoutRepository: CheckoutRepository) {
    post("/checkout/pay") {
        val parameters = call.receiveParameters()
        val city = parameters["city"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val state = parameters["state"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val country = parameters["country"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val address = Address(city, state, country)

        val principal = call.principal<UserIdPrincipal>()!!
        val updated = checkoutRepository.updateUserAddress(principal.email, address)

        if (updated) {
            // Start payment
            val success = true // result of payment
            if (success) {
                val result = checkoutRepository.checkoutSuccess(principal.email)
                if (result) call.respondText("/tracking")
            } else {
                call.respondText("Payment not successful")
            }
        }
    }
}