package com.example.features.notification.routes

import com.example.features.notification.data.NotificationRepository
import com.example.model.UserPrincipal
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllNotifications(notificationRepository: NotificationRepository) {
    get {
        val principal = call.principal<UserPrincipal>()!!
        val notifications = notificationRepository.getAllNotifications(principal.email)
        call.respond(notifications)
    }
}
