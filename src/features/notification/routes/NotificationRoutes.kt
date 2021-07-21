package com.example.features.notification.presentation

import com.example.features.notification.data.NotificationRepository
import com.example.features.notification.routes.getAllNotifications
import com.example.features.notification.routes.getNotification
import com.example.util.constants.Auth.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerNotificationRoutes() {

    val notificationRepository by inject<NotificationRepository>()

    routing {
        route("/notifications") {
            authenticate(USER_AUTH) {
                getAllNotifications(notificationRepository)
                getNotification(notificationRepository)
            }
        }
    }
}


