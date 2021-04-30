package com.example.feautures.wishlist.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerWishlistRoutes() {

    routing {
        wishlistRoute()
    }
}

fun Route.wishlistRoute(){

}
