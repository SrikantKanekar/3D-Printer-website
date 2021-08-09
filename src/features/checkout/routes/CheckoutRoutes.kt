package com.example.features.checkout.presentation

import com.example.config.AppConfig
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.routes.checkoutGet
import com.example.features.checkout.routes.checkoutVerify
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCheckoutRoute() {

    val checkoutRepository by inject<CheckoutRepository>()
    val appConfig by inject<AppConfig>()

    routing {
        route("/checkout") {
            authenticate(USER_AUTH) {
                checkoutGet(checkoutRepository)
                checkoutProceed(checkoutRepository, appConfig)
                checkoutVerify(checkoutRepository, appConfig)
            }
        }
    }
}

