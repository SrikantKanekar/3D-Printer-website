package com.example.features.admin.presentation

import com.example.config.AppConfig
import com.example.features.admin.data.AdminRepository
import com.example.util.enums.OrderStatus
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateOrderStatus(adminRepository: AdminRepository, appConfig: AppConfig) {
    put("/order-status/{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val orderStatus = call.receive<OrderStatus>()
        val updated = adminRepository.updateOrderStatus(id, orderStatus, appConfig)

        when (updated) {
            true -> call.respond(orderStatus)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Order status not updated"
            )
        }
    }
}