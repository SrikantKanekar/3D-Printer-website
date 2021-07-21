package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllActiveOrders(orderRepository: AdminRepository) {
    get {
        val activeOrders = orderRepository.getAllActiveOrders()
        call.respond(activeOrders)
    }
}