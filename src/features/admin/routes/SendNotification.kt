package com.example.features.order.presentation

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.requests.NotificationRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.sendNotification(adminRepository: AdminRepository) {
    post("/notification") {
        val body = call.receive<NotificationRequest>()
        val result = adminRepository.sendNotification(body)
        when (result) {
            true -> call.respond(body)
            false -> call.respond(
                HttpStatusCode.InternalServerError,
                "Notification not sent"
            )
        }
    }
}
