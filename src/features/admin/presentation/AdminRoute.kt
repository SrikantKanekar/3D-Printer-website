package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.Constants
import com.example.features.checkout.domain.OrderStatus.*
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerAdminRoutes() {

    val adminRepository by inject<AdminRepository>()

    routing {
        getAdminLoginRoute()
        postAdminLoginRoute()
        authenticate("ADMIN_AUTH") {
            getAdminRoute(adminRepository)
            postStatusRoute(adminRepository)
        }
    }
}

private fun Route.getAdminLoginRoute() {
    get("/admin/login") {
        call.respond(FreeMarkerContent("admin_login.ftl", null))
    }
}

private fun Route.postAdminLoginRoute() {
    post("/admin/login") {
        val params = call.receiveParameters()
        val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["Password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (name == "admin" && password == "password") {
            call.sessions.set(AdminPrincipal(name))
            call.respondText("/admin")
        } else {
            call.respondText(Constants.EMAIL_PASSWORD_INCORRECT)
        }
    }
}

private fun Route.getAdminRoute(adminRepository: AdminRepository) {
    get("/admin") {

        val processingOrders = adminRepository.getProcessingOrders()
        val historyOrders = adminRepository.getOrderHistory()
        call.respond(
            FreeMarkerContent(
                "admin_panel.ftl", mapOf(
                    "processingOrders" to processingOrders, "historyOrders" to historyOrders
                )
            )
        )
    }
}

private fun Route.postStatusRoute(adminRepository: AdminRepository) {
    post("/admin/printing") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val order = adminRepository.getProcessingOrder(id)
        if (order != null) {
            //val result = adminRepository.updateProcessingOrder(order.copy(status = PRINTING))
            //call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/printed") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val order = adminRepository.getProcessingOrder(id)
        if (order != null) {
            //val result = adminRepository.updateProcessingOrder(order.copy(status = PRINTED))
            //call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/delivering") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val order = adminRepository.getProcessingOrder(id)
        if (order != null) {
            //val result = adminRepository.updateProcessingOrder(order.copy(status = DELIVERING))
            //call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/delivered") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
            val result = adminRepository.orderDelivered(id)
        if (result){
            call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

}

