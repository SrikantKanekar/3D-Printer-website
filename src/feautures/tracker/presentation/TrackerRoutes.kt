package com.example.feautures.tracker.presentation

import io.ktor.application.*
import io.ktor.routing.*

fun Application.registerTrackerRoutes() {

    routing {
        trackerRoute()
    }
}

fun Route.trackerRoute(){

}
