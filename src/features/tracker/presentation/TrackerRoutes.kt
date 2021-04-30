package com.example.features.tracker.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerTrackerRoutes() {

    routing {
        trackerRoute()
    }
}

fun Route.trackerRoute(){

}
