package com.example.features.tracking.presentation

import com.example.features.auth.domain.UserPrincipal
import com.example.features.tracking.data.TrackingRepository
import com.example.util.AUTH.USER_SESSION_AUTH
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.freemarker.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject

fun Application.registerTrackerRoutes() {

    val trackingRepository by inject<TrackingRepository>()

    routing {
        authenticate(USER_SESSION_AUTH) {
            getTrackingRoute(trackingRepository)
        }
    }
}

fun Route.getTrackingRoute(trackingRepository: TrackingRepository) {
    get("/tracking") {

        val principal = call.principal<UserPrincipal>()!!
        val obj = trackingRepository.getUserTrackingobjects(principal.email)
        call.respond(
            FreeMarkerContent(
                "tracking.ftl",
                mapOf("objects" to obj, "user" to principal)
            )
        )
    }
}
