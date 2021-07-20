package com.example.features.order.routes

import com.example.features.order.data.OrderRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getOrders(orderRepository: OrderRepository) {
    get("/orders") {
        val principal = call.principal<UserPrincipal>()!!
        val orders = orderRepository.getUserOrders(principal.email)
        call.respond(orders)
    }
}