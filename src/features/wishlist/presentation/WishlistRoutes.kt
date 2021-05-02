package com.example.features.wishlist.presentation

import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.wishlist.data.WishlistRepository
import com.example.features.wishlist.domain.WishlistCookie
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerWishlistRoutes() {

    val wishlistRepository by inject<WishlistRepository>()

    routing {
        getWishlistRoute(wishlistRepository)
        deleteFromWishlistRoute(wishlistRepository)
        addToCartRoute(wishlistRepository)
    }
}

fun Route.getWishlistRoute(wishlistRepository: WishlistRepository) {
    get("/wishlist") {

        val principal = call.sessions.get<UserIdPrincipal>()
        val ordersIds = when (principal) {
            null -> {
                val cookie = call.sessions.get<WishlistCookie>() ?: WishlistCookie()
                cookie.orders
            }
            else -> wishlistRepository.getUserWishlist(principal.email)
        }
        val orders = wishlistRepository.getOrderList(ordersIds)
        call.respond(FreeMarkerContent("wishlist.ftl", mapOf("orders" to orders)))
    }
}

private fun Route.deleteFromWishlistRoute(wishlistRepository: WishlistRepository) {
    get("/wishlist/{id}/delete") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val orderResult: Boolean
        val userResult: Boolean

        val principal = call.sessions.get<UserIdPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<WishlistCookie>() ?: WishlistCookie()
                userResult = cookie.orders.remove(id)
                orderResult = wishlistRepository.deleteWishlist(id)
                call.sessions.set(cookie)
            }
            else -> {
                orderResult = wishlistRepository.deleteWishlist(id)
                userResult = wishlistRepository.deleteUserWishlist(principal.email, id)
            }
        }
        if (userResult and orderResult) {
            call.respondRedirect("/wishlist")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
        }
    }
}

private fun Route.addToCartRoute(wishlistRepository: WishlistRepository) {
    get("/wishlist/{id}/cart") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.sessions.get<UserIdPrincipal>()
        when (principal) {
            null -> {
                call.respondRedirect {
                    path("auth/login")
                    parameters.append("returnUrl", call.request.uri)
                }
            }
            else -> {
                val result = wishlistRepository.addToCart(principal.email, id)
                if (result) {
                    call.respondRedirect("/wishlist")
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "Invalid Order ID")
                }
            }
        }
    }
}
