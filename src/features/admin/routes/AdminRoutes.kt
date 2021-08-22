package com.example.features.admin.presentation

import com.example.config.AppConfig
import com.example.features.admin.data.AdminRepository
import com.example.features.admin.routes.fulfillRequests
import com.example.features.admin.routes.getActiveRequests
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
    val appConfig by inject<AppConfig>()

    routing {
        route("/admin") {
            authenticate(ADMIN_AUTH) {
                getAllActiveOrders(adminRepository)
                sendNotification(adminRepository, appConfig)
                updateOrderStatus(adminRepository, appConfig)
                updatePrintingStatus(adminRepository, appConfig)

                getActiveRequests(adminRepository)
                fulfillRequests(adminRepository)
            }
        }
    }
}