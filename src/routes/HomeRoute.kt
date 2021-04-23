package com.example.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.homeRoute(){

    get("/") {
        call.respondText("3d printer", contentType = ContentType.Text.Plain)
    }
}