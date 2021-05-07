package com.example.features.tracker.presentation

import com.example.features.auth.domain.UserIdPrincipal
import com.example.features.tracker.data.TrackerRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerTrackerRoutes() {

    val trackerRepository by inject<TrackerRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getTrackerRoute(trackerRepository)
        }
    }
}

fun Route.getTrackerRoute(trackerRepository: TrackerRepository) {
    get("/tracking") {

        val principal = call.principal<UserIdPrincipal>()!!
        val orders = trackerRepository.getUserTrackingOrders(principal.email)
        call.respond(
            FreeMarkerContent(
                "tracking.ftl",
                mapOf("orders" to orders, "user" to principal)
            )
        )
    }
}
