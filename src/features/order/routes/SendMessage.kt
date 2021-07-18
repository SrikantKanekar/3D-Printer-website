package com.example.features.order.presentation

import com.example.features.order.data.OrderRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.sendCustomMessage(orderRepository: OrderRepository) {
    post("/order/send-message") {

        val params = call.receiveParameters()
        val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val title = params["title"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val message = params["message"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val result = orderRepository.sendCustomNotification(email, title, message)
        call.respond(result)
    }
}
