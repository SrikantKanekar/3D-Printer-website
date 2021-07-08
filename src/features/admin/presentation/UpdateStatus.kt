package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.order.domain.OrderStatus
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updateOrderStatus(adminRepository: AdminRepository) {
    post("/admin/update/order-status") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val status = params["order_status"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val orderStatus = OrderStatus.values()[status]

        val updated = adminRepository.updateOrderStatus(id, orderStatus)
        call.respond(updated)
    }
}