package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.model.Setting
import com.example.model.SpecialRequest
import com.example.model.UserPrincipal
import com.example.util.now
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.objectSpecialRequest(objectRepository: ObjectRepository) {
    post("/requests/special/{id}") {
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
                val request = SpecialRequest(
                    _id = obj.id,
                    userEmail = principal.email,
                    fileUrl = obj.fileUrl,
                    fileExtension = obj.fileExtension,
                    imageUrl = obj.imageUrl,
                    setting = obj.setting,
                    requestedAt = now()
                )
                objectRepository.sendSpecialRequest(request)
            } else return@post call.respond(HttpStatusCode.NotFound)
        } else return@post call.respond(HttpStatusCode.MethodNotAllowed)

        call.respond(HttpStatusCode.Created)
    }
}
