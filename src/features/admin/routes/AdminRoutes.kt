package com.example.features.admin.presentation

import com.example.features.admin.data.AdminPrincipal
import com.example.features.admin.data.AdminRepository
import com.example.model.UserPrincipal
import com.example.util.constants.Auth.ADMIN_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerAdminRoutes() {

    val adminRepository by inject<AdminRepository>()

    routing {
        getAdminLoginRoute()
        postAdminLoginRoute()

        authenticate(ADMIN_AUTH) {
            getAdminRoute(adminRepository)
            updateOrderStatus(adminRepository)
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