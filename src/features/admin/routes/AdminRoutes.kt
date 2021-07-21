package com.example.features.admin.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.routes.getAllActiveOrders
import com.example.features.order.presentation.sendNotification
import com.example.features.order.presentation.updatePrintingStatus
import com.example.util.constants.Auth.ADMIN_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerAdminRoutes() {

    val adminRepository by inject<AdminRepository>()

    routing {
        route("/admin"){
            authenticate(ADMIN_AUTH) {
                getAllActiveOrders(adminRepository)
                sendNotification(adminRepository)
                updateOrderStatus(adminRepository)
                updatePrintingStatus(adminRepository)
            }
        }
    }
}