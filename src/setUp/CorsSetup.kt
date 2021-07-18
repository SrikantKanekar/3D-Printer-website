package com.example.setUp

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.corsSetup() {
    install(CORS){
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Patch)
        method(HttpMethod.Delete)
        header(HttpHeaders.Authorization)
        header(HttpHeaders.ContentType)
        anyHost()
    }
}