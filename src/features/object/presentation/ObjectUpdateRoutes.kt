package com.example.features.`object`.presentation

import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
import com.example.features.auth.domain.UserPrincipal
import com.example.features.userObject.domain.ObjectsCookie
import com.example.util.FileHandler
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.getUpdateObjectRoute(objectRepository: ObjectRepository) {
    get("/object/{id}") {
        val id = call.parameters["id"]!!
        val principal = call.sessions.get<UserPrincipal>()
        val obj = if (principal != null) {
            objectRepository.getUserObject(principal.email, id)
        } else {
            call.sessions.get<ObjectsCookie>()?.objects?.find { it.id == id }
        }
        if (obj != null) {
            call.respond(
                FreeMarkerContent(
                    "object_update.ftl", mapOf(
                        "object" to obj,
                        "user" to (principal?.email ?: "")
                    )
                )
            )
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid object ID")
        }
    }
}

fun Route.updateBasicSettingsRoute(objectRepository: ObjectRepository) {
    post("/object/{id}/basic") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val size = params["size"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val basicSettings = BasicSettings(size = size)

        val principal = call.sessions.get<UserPrincipal>()
        var updated = false
        if (principal != null) {
            updated = objectRepository.updateBasicSettings(principal.email, id, basicSettings)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            cookie.objects
                .find { it.id == id }
                ?.let {
                    it.basicSettings = basicSettings
                    updated = true
                }
            call.sessions.set(cookie)
        }
        when (updated) {
            true -> call.respond(basicSettings)
            false -> call.respond(HttpStatusCode.NotAcceptable, "Invalid object ID")
        }
    }
}

fun Route.updateAdvancedSettingsRoute(objectRepository: ObjectRepository) {
    post("/object/{id}/advanced") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val weight = params["weight"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val advancedSettings = AdvancedSettings(weight = weight)

        val principal = call.sessions.get<UserPrincipal>()
        var updated = false
        if (principal != null) {
            updated = objectRepository.updateAdvancedSettings(principal.email, id, advancedSettings)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            cookie.objects
                .find { it.id == id }
                ?.let {
                    it.advancedSettings = advancedSettings
                    updated = true
                }
            call.sessions.set(cookie)
        }
        when (updated) {
            true -> call.respond(advancedSettings)
            false -> call.respond(HttpStatusCode.NotAcceptable, "Invalid object ID")
        }
    }
}
