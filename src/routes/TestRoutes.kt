package com.example.routes

import com.example.data.models.IndexData
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.testRoute(){

    get("/hello-world") {
        call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
    }

    get("/freemarker") {
        call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
    }
}
