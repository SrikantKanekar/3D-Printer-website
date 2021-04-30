package com.example.features.wishlist.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerWishlistRoutes() {

    routing {
        wishlistRoute()
    }
}

fun Route.wishlistRoute(){

}
