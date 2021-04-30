package com.example.feautures.notification.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerNotificationRoutes() {

    routing {
        notificationRoute()
    }
}

fun Route.notificationRoute(){

}
