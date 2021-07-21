package com.example.features.checkout.presentation

import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.routes.checkoutGet
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCheckoutRoute() {

    val checkoutRepository by inject<CheckoutRepository>()

    routing {
        route("/checkout") {
            authenticate(USER_AUTH) {
                checkoutGet(checkoutRepository)
                checkoutProceed(checkoutRepository)
            }
        }
    }
}

