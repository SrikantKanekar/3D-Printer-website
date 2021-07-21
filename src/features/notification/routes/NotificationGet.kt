package com.example.features.notification.routes

import com.example.features.notification.data.NotificationRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getNotification(notificationRepository: NotificationRepository) {
    get("{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val principal = call.principal<UserPrincipal>()!!
        val notification = notificationRepository.getNotificationById(principal.email, id)

        when(notification) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(notification)
        }
    }
}
