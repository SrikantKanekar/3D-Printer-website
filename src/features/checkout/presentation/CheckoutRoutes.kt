package com.example.features.checkout.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerCheckoutRoutes() {

    routing {
        checkoutRoute()
    }
}

fun Route.checkoutRoute(){

}
