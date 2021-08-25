package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getActiveObjects(adminRepository: AdminRepository) {

    get("/objects") {
        val requests = adminRepository.getActiveObjectRequests()
        call.respond(requests)
    }

    get("/objects/{email}/{id}") {
        val id = call.parameters["id"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val email = call.parameters["email"] ?: return@get call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed email"
        )

        val requests = adminRepository.getObjectById(email, id)

        when (requests) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> call.respond(requests)
        }
    }
}