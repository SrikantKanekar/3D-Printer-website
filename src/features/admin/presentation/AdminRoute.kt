package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.Constants
import com.example.features.order.domain.OrderStatus
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

fun Application.registerAdminRoutes() {

    val adminRepository by inject<AdminRepository>()

    routing {
        getAdminLoginRoute()
        postAdminLoginRoute()
        authenticate(ADMIN_SESSION_AUTH) {
            getAdminRoute(adminRepository)
            updateOrderStatus(adminRepository)
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

        val principal = call.principal<AdminPrincipal>()!!
        val activeOrders = adminRepository.getAllActiveOrders()

        call.respond(
            FreeMarkerContent(
                "admin.ftl", mapOf(
                    "activeOrders" to activeOrders,
                    "admin" to principal
                )
            )
        )
    }
}

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