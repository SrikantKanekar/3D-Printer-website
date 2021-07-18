package com.example.setUp

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*

fun Application.contentNegotiationSetup() {
    install(ContentNegotiation) {
        json()
    }
}