package com.example.features.order.presentation

import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.UserPrincipal
import com.example.features.order.data.OrderRepository
import com.example.util.AUTH.ADMIN_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerOrderRoute() {

    val orderRepository by inject<OrderRepository>()

    routing {
        getOrderRoute(orderRepository)
        authenticate(ADMIN_AUTH) {
            updatePrintingStatus(orderRepository)
            sendCustomMessage(orderRepository)
        }
    }
}

fun Route.getOrderRoute(orderRepository: OrderRepository) {
    get("/order/{id}") {

        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )

        val adminPrincipal = call.sessions.get<AdminPrincipal>()
        val userPrincipal = call.sessions.get<UserPrincipal>()

        val order = when {
            adminPrincipal != null -> {
                orderRepository.getOrderForAdmin(id)
            }
            userPrincipal != null -> {
                orderRepository.getOrderForUser(userPrincipal.email, id)
            }
            else -> null
        }
        order?.let {
            val objects = orderRepository.getUserOrderObjects(order)
            call.respond(
                FreeMarkerContent(
                    "order.ftl",
                    mapOf(
                        "order" to order,
                        "objects" to objects,
                        "user" to (userPrincipal?.email ?: ""),
                        "admin" to (adminPrincipal?.name ?: "")
                    )
                )
            )
        }
    }
}