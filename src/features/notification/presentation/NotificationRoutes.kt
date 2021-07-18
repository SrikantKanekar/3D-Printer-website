package com.example.features.notification.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.notification.data.NotificationRepository
import com.example.util.AUTH.USER_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerNotificationRoutes() {

    val notificationRepository by inject<NotificationRepository>()

    routing {
        authenticate(USER_AUTH) {
            getAllNotificationsRoute(notificationRepository)
            getNotificationRoute(notificationRepository)
        }
    }
}

fun Route.getAllNotificationsRoute(notificationRepository: NotificationRepository) {
    get("/notification") {
        val principal = call.principal<UserPrincipal>()!!
        val notifications = notificationRepository.getAllNotifications(principal.email)
        call.respond(
            FreeMarkerContent(
                "notification.ftl",
                mapOf("notifications" to notifications, "user" to principal)
            )
        )
    }
}

private fun Route.getNotificationRoute(notificationRepository: NotificationRepository) {
    get("/notification/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val principal = call.principal<UserPrincipal>()!!
        val notification = notificationRepository.getNotificationById(principal.email, id)
        notification?.let {
            call.respond(
                FreeMarkerContent(
                    "notification_detail.ftl",
                    mapOf("notification" to notification, "user" to principal)
                )
            )
        }
    }
}
