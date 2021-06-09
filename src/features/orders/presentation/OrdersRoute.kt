package com.example.features.orders.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.orders.data.OrdersRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerOrdersRoute() {

    val ordersRepository by inject<OrdersRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getOrdersRoute(ordersRepository)
        }
    }
}

fun Route.getOrdersRoute(ordersRepository: OrdersRepository) {
    get("/orders") {

        val principal = call.principal<UserPrincipal>()!!
        val orders = ordersRepository.getUserOrders(principal.email)
        call.respond(
            FreeMarkerContent(
                "orders.ftl",
                mapOf("orders" to orders, "user" to principal)
            )
        )
    }
}
