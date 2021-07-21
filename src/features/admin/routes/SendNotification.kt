package com.example.features.order.presentation

import com.example.config.AppConfig
import com.example.features.admin.data.AdminRepository
import com.example.features.admin.requests.NotificationRequest
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.sendNotification(adminRepository: AdminRepository, appConfig: AppConfig) {
    post("/notification") {
        val body = call.receive<NotificationRequest>()
        adminRepository.sendNotification(body, appConfig)
        call.respond(body)
    }
}
