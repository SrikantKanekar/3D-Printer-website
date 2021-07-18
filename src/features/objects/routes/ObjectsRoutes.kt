package com.example.features.objects.presentation

import com.example.features.objects.data.ObjectsRepository
import com.example.model.ObjectsCookie
import com.example.model.UserPrincipal
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
            ArrayList(cookie.objects)
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
