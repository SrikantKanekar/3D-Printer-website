package com.example.setUp

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.staticFilesSetup() {
    routing {
        static("static") {
            resources("static")
        }
    }
}