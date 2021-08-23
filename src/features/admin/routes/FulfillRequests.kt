package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import com.example.features.admin.requests.DirectFulfillRequest
import com.example.features.admin.requests.SpecialFulfillRequest
import com.example.model.Object
import com.example.model.Slicing
import com.example.util.calculateSlicingDetails
import com.example.util.now
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.fulfillRequests(adminRepository: AdminRepository) {
    put("/requests/direct/{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val body = call.receive<DirectFulfillRequest>()
        var slicing = Slicing(
            calculateSlicingDetails(body._super),
            calculateSlicingDetails(body.dynamic),
            calculateSlicingDetails(body.standard),
            calculateSlicingDetails(body.low)
        )

        val request = adminRepository.getDirectRequestById(id)
        when (request) {
            null -> call.respond(HttpStatusCode.NotFound, "Direct Request not found")
            else -> {
                // update Request
                request.apply {
                    fulfilled = true
                    slicing = slicing
                    fulfilledAt = now()
                }
                adminRepository.updateDirectRequest(request)

                // Add object to user
                val newObject = Object(
                    id = request._id,
                    name = request.name,
                    fileUrl = request.fileUrl,
                    fileExtension = request.fileExtension,
                    imageUrl = request.imageUrl,
                    slicing = slicing
                )
                val user = adminRepository.getUser(request.userEmail)
                user.objects.add(newObject)
                adminRepository.updateUser(user)

                call.respond(HttpStatusCode.OK)
            }
        }
    }

    put("/requests/special/{id}") {
        val id = call.parameters["id"] ?: return@put call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val body = call.receive<SpecialFulfillRequest>()
        var slicingDetails = calculateSlicingDetails(body)

        val request = adminRepository.getSpecialRequestById(id)
        when (request) {
            null -> call.respond(HttpStatusCode.NotFound, "Special Request not found")
            else -> {

                // Update Request
                request.apply {
                    fulfilled = true
                    slicingDetails = slicingDetails
                    fulfilledAt = now()
                }
                adminRepository.updateSpecialRequest(request)

                // Update user
                val user = adminRepository.getUser(request.userEmail)
                user.objects
                    .find { it.id == request._id }
                    ?.apply { slicing.custom = slicingDetails }
                    ?: call.respond(HttpStatusCode.NotFound, "User Object not found")
                adminRepository.updateUser(user)

                call.respond(HttpStatusCode.OK)
            }
        }
    }
}