package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.Request
import com.example.model.Setting
import com.example.model.UserPrincipal
import com.example.util.now
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectSendRequest(objectRepository: ObjectRepository) {
    post("/request/{id}") {
        val id = call.parameters["id"] ?: return@post call.respond(
            status = HttpStatusCode.BadRequest,
            message = "Missing or malformed id"
        )
        val body = call.receive<Setting>()
        val principal = call.principal<UserPrincipal>()!!

        val updated = objectRepository.updateSetting(principal.email, id, body)

        if (updated) {
            val obj = objectRepository.getUserObjectById(principal.email, id)
            if (obj != null) {
                val request = Request(
                    _id = obj.id,
                    userEmail = principal.email,
                    fileUrl = obj.fileUrl,
                    setting = obj.setting,
                    requestedAt = now()
                )
                objectRepository.sendRequest(request)
            } else return@post call.respond(HttpStatusCode.NotFound)
        } else return@post call.respond(HttpStatusCode.MethodNotAllowed)

        call.respond(body)
    }
}
