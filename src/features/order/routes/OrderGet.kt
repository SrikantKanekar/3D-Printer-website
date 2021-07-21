package com.example.features.order.routes

import com.example.features.order.data.OrderRepository
import com.example.features.order.response.OrderObjectResponse
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getOrder(orderRepository: OrderRepository) {
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val principal = call.principal<UserPrincipal>()!!
        val order = orderRepository.getOrderForUserOrAdmin(principal, id)

        when (order) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> {
                val objects = orderRepository.getOrderObjects(order)
                call.respond(OrderObjectResponse(order, objects))
            }
        }
    }
}