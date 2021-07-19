package com.example.features.cart.presentation

import com.example.features.`object`.presentation.cartAdd
import com.example.features.cart.data.CartRepository
import com.example.features.cart.routes.getCart
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerCartRoute() {

    val cartRepository by inject<CartRepository>()

    routing {
        authenticate(USER_AUTH) {
            getCart(cartRepository)
            cartAdd(cartRepository)
            cartDelete(cartRepository)
        }
    }
}

