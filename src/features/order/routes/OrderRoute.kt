package com.example.features.order.presentation

import com.example.features.order.data.OrderRepository
import com.example.features.order.routes.getOrder
import com.example.features.order.routes.getOrders
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerOrderRoute() {

    val orderRepository by inject<OrderRepository>()

    routing {
        route("/orders") {
            authenticate(USER_AUTH) {
                getOrders(orderRepository)
                getOrder(orderRepository)
            }
        }
    }
}