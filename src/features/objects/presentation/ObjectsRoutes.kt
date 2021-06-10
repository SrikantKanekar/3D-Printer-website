package com.example.features.objects.presentation

import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.auth.domain.UserPrincipal
import com.example.features.objects.data.ObjectsRepository
import com.example.features.objects.domain.ObjectsCookie
import com.example.util.FileHandler.deleteFile
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject

fun Application.registerObjectsRoute() {

    val objectsRepository by inject<ObjectsRepository>()

    routing {
        getObjectsRoute(objectsRepository)
        deleteObject(objectsRepository)
        addToCart(objectsRepository)
    }
}

private fun Route.getObjectsRoute(objectsRepository: ObjectsRepository) {
    get("/objects") {

        val principal = call.sessions.get<UserPrincipal>()
        val objs = if (principal != null) {
            objectsRepository.getUserObjects(principal.email)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            ArrayList(cookie.objects.filter { it.status == NONE })
        }
        call.respond(
            FreeMarkerContent(
                "objects.ftl",
                mapOf(
                    "objects" to objs,
                    "user" to (principal?.email ?: "")
                )
            )
        )
    }
}

private fun Route.deleteObject(objectsRepository: ObjectsRepository) {
    get("/objects/{id}/delete") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        var deleteFileResult = false

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> {
                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                val deleted = cookie.objects.removeIf { it.id == id && it.status == NONE }
                call.sessions.set(cookie)
                if (deleted) deleteFileResult = deleteFile(id)
            }
            else -> {
                val deleted = objectsRepository.deleteUserObject(principal.email, id)
                if (deleted) deleteFileResult = deleteFile(id)
            }
        }
        if (deleteFileResult) {
            call.respondRedirect("/objects")
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid object ID")
        }
    }
}

private fun Route.addToCart(objectsRepository: ObjectsRepository) {
    post("/objects/add-to-cart") {
        val param = call.receiveParameters()
        val id = param["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)

        val principal = call.sessions.get<UserPrincipal>()
        when (principal) {
            null -> {
                call.respondText("/auth/login?returnUrl=/objects")
            }
            else -> {
                val result = objectsRepository.addToCart(principal.email, id)
                call.respondText(result.toString())
            }
        }
    }
}
