package com.example.features.cart.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerCartRoutes() {

    routing {
        cartRoute()
    }
}

fun Route.cartRoute(){

}