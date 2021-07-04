package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.domain.AdminPrincipal
import com.example.features.auth.domain.AuthConstants
import com.example.features.auth.domain.UserPrincipal
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

        /**
         * redirect to admin screen if already logged in as admin
         */
        val adminPrincipal = call.sessions.get<AdminPrincipal>()
        if (adminPrincipal != null) {
            call.respondRedirect("/admin")
        } else {
            val userPrincipal = call.sessions.get<UserPrincipal>()
            call.respond(
                FreeMarkerContent(
                    "admin_login.ftl",
                    mapOf("user" to (userPrincipal?.email ?: ""))
                )
            )
        }
    }
}

private fun Route.postAdminLoginRoute() {
    post("/admin/login") {
        val params = call.receiveParameters()
        val name = params["name"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val password = params["password"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        if (name == "admin" && password == "password") {
            call.sessions.set(AdminPrincipal(name))
            call.respondText("/admin")
        } else {
            call.respondText(AuthConstants.EMAIL_PASSWORD_INCORRECT)
        }
    }
}

private fun Route.getAdminRoute(adminRepository: AdminRepository) {
    get("/admin") {

        val adminPrincipal = call.principal<AdminPrincipal>()!!
        val userPrincipal = call.sessions.get<UserPrincipal>()
        val activeOrders = adminRepository.getAllActiveOrders()

        call.respond(
            FreeMarkerContent(
                "admin.ftl", mapOf(
                    "activeOrders" to activeOrders,
                    "admin" to adminPrincipal,
                    "user" to (userPrincipal?.email ?: "")
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