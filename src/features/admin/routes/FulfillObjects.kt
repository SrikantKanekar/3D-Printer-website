package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.requests.DirectFulfillRequest
import com.example.model.Slicing
import com.example.util.calculateSlicingDetails
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.fulfillObjects(adminRepository: AdminRepository) {
    put("/objects/{email}/{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )

        val email = call.parameters["email"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed email"
        )

        val obj = adminRepository.getObjectById(email, id)

        when (obj) {
            null -> call.respond(HttpStatusCode.NotFound)
            else -> {
                val body = call.receive<DirectFulfillRequest>()
                val slicing = Slicing(
                    sliced = true,
                    calculateSlicingDetails(body._super),
                    calculateSlicingDetails(body.dynamic),
                    calculateSlicingDetails(body.standard),
                    calculateSlicingDetails(body.low),
                    obj.slicing.custom
                )
                adminRepository.updateObjectRequest(email, id, slicing)
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}