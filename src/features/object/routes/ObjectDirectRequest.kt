package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.requests.ObjectDirectRequest
import com.example.model.DirectRequest
import com.example.model.UserPrincipal
import com.example.util.now
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectDirectRequest(objectRepository: ObjectRepository) {
    post("/request/direct/{id}") {
        val id = call.parameters["id"] ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val body = call.receive<ObjectDirectRequest>()
        val principal = call.principal<UserPrincipal>()!!

        val request = DirectRequest(
            _id = id,
            name = body.name,
            fileUrl = body.fileUrl,
            fileExtension = body.fileExtension,
            imageUrl = body.imageUrl,
            userEmail = principal.email,
            requestedAt = now()
        )
        objectRepository.sendDirectRequest(request)

        call.respond(HttpStatusCode.Created)
    }
}
