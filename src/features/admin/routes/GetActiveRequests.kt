package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getActiveRequests(adminRepository: AdminRepository) {

    get("/requests/direct") {
        val requests = adminRepository.getActiveDirectRequests()
        call.respond(requests)
    }

    get("/requests/direct/{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val requests = adminRepository.getDirectRequestById(id)

        when (requests) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(requests)
        }
    }

    get("/requests/special") {
        val requests = adminRepository.getActiveSpecialRequests()
        call.respond(requests)
    }

    get("/requests/special/{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val requests = adminRepository.getSpecialRequestById(id)

        when (requests) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(requests)
        }
    }
}