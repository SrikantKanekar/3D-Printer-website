package com.example.features.checkout.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.checkout.data.CheckoutRepository
import com.example.util.AUTH.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCheckoutRoute() {

    val checkoutRepository by inject<CheckoutRepository>()

    routing {
        authenticate(USER_AUTH) {
            getCheckoutRoute(checkoutRepository)
            updateAddress(checkoutRepository)
            paymentSucceed(checkoutRepository)
        }
    }
}

private fun Route.getCheckoutRoute(checkoutRepository: CheckoutRepository) {
    get("/checkout") {

        val principal = call.principal<UserPrincipal>()!!
        val address = checkoutRepository.getUserAddress(principal.email)
        val objects = checkoutRepository.getCartObjects(principal.email)

        call.respond(
            FreeMarkerContent(
                "checkout.ftl", mapOf(
                    "user" to principal,
                    "objects" to objects,
                    "address" to address
                )
            )
        )
    }
}