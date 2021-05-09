package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.Constants
import com.example.features.checkout.domain.OrderStatus.*
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

        val activeOrders = adminRepository.getAllActiveOrders()
        val completedOrders = adminRepository.getAllCompletedOrders()
        call.respond(
            FreeMarkerContent(
                "admin_panel.ftl", mapOf(
                    "activeOrders" to activeOrders, "completedOrders" to completedOrders
                )
            )
        )
    }
}

private fun Route.postStatusRoute(adminRepository: AdminRepository) {
    post("/admin/printing") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val result = adminRepository.updateTrackingStatus(id, PRINTING)
        if (result) {
            call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/printed") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val result = adminRepository.updateTrackingStatus(id, PRINTED)
        if (result) {
            call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/delivering") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val result = adminRepository.updateTrackingStatus(id, DELIVERING)
        if (result) {
            call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }

    post("/admin/delivered") {
        val params = call.receiveParameters()
        val id = params["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val result = adminRepository.updateTrackingStatus(id, DELIVERED)
        if (result) {
            call.respondText(result.toString())
        } else {
            call.respondText("Invalid order ID")
        }
    }
}

