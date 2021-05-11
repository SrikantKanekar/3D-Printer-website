package com.example.features.order.presentation

import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.UserPrincipal
import com.example.features.order.data.OrderRepository
import com.example.features.order.domain.PrintingStatus
import com.example.util.AUTH.ADMIN_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerOrderRoutes() {

    val orderRepository by inject<OrderRepository>()

    routing {
        getOrderRoute(orderRepository)
        authenticate(ADMIN_SESSION_AUTH) {
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
            call.respond(
                FreeMarkerContent(
                    "order.ftl",
                    mapOf(
                        "order" to order,
                        "user" to (userPrincipal?.email ?: ""),
                        "admin" to (adminPrincipal?.name ?: "")
                    )
                )
            )
        }
    }
}

fun Route.updatePrintingStatus(objectRepository: OrderRepository) {
    post("/order/update/printing-status") {
        val params = call.receiveParameters()
        val orderId = params["orderId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val objectId = params["objectId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val status = params["printing_status"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val printingStatus = PrintingStatus.values()[status]

        val updated = objectRepository.updatePrintingStatus(orderId, objectId, printingStatus)
        when (updated) {
            true -> call.respond("updated")
            false -> call.respondText("Not updated")
        }
    }
}

private fun Route.sendCustomMessage(orderRepository: OrderRepository) {
    post("/order/send-message") {
        val params = call.receiveParameters()
        val email = params["email"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val title = params["title"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val message = params["message"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        when (orderRepository.sendCustomNotification(email, title, message)) {
            true -> call.respondText("Notification sent")
            false -> call.respondText("Not successful")
        }
    }
}
