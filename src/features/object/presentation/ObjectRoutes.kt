package com.example.features.`object`.presentation

import com.example.features.userObject.domain.ObjectsCookie
import com.example.features.auth.domain.UserPrincipal
import com.example.features.`object`.data.ObjectRepository
import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
import io.ktor.application.*
import io.ktor.freemarker.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import org.koin.ktor.ext.inject
import java.io.File

fun Application.registerObjectRoutes() {

    val objectRepository by inject<ObjectRepository>()

    routing {
        getCreateObjectRoute()
        createObjectRoute(objectRepository)
        getUpdateObjectRoute(objectRepository)
        updateFileRoute(objectRepository)
        updateBasicSettingsRoute(objectRepository)
        updateAdvancedSettingsRoute(objectRepository)
    }
}

fun Route.getCreateObjectRoute() {
    get("/order") {
        val principal = call.sessions.get<UserPrincipal>()
        call.respond(FreeMarkerContent("order_create.ftl", mapOf("user" to (principal?.email ?: ""))))
    }
}

/**
1.Create a new order.

2.If user is logged in, save orderId in user database, else save in cookie.

3.Upload the file in 'uploads' folder with name orderId.
 **/
fun Route.createObjectRoute(objectRepository: ObjectRepository) {
    post("/order/create") {
        val multipartData = call.receiveMultipart()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {

                val fileName = part.originalFileName!!
                val obj = objectRepository.createNewObject(fileName)
                val file = File("uploads/${obj.id}")
                try {
                    part.streamProvider().use { inputStream ->
                        file.outputStream().buffered().use { outputStream ->
                            inputStream.copyTo(outputStream)

                            val principal = call.sessions.get<UserPrincipal>()
                            if (principal != null) {
                                objectRepository.addUserObject(principal.email, obj)
                            } else {
                                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                                cookie.objects.add(obj)
                                call.sessions.set(cookie)
                            }
                            call.respondRedirect("/order/${obj.id}")
                        }
                    }
                } catch (e: Exception) {
                    file.delete()
                    call.respondText("Upload not successful")
                }
            }
            part.dispose()
        }
    }
}

fun Route.getUpdateObjectRoute(objectRepository: ObjectRepository) {
    get("/order/{id}") {
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
                    "order_update.ftl", mapOf(
                        "order" to obj,
                        "user" to (principal?.email ?: "")
                    )
                )
            )
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid order ID")
        }
    }
}

fun Route.updateFileRoute(objectRepository: ObjectRepository) {
    post("/order/{id}/file") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val multipartData = call.receiveMultipart()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileName = part.originalFileName!!
                val file = File("uploads/$id")

                if (file.exists()) {
                    file.delete()
                    try {
                        part.streamProvider().use { inputStream ->
                            file.outputStream().buffered().use { outputStream ->
                                inputStream.copyTo(outputStream)

                                val principal = call.sessions.get<UserPrincipal>()
                                if (principal != null) {
                                    objectRepository.updateFileName(principal.email, id, fileName)
                                } else {
                                    val cookie = call.sessions.get<ObjectsCookie>()!!
                                    cookie.objects.find { it.id == id }?.fileName = fileName
                                    call.sessions.set(cookie)
                                }
                                call.respondText("Successfully updated")
                            }
                        }
                    } catch (e: Exception) {
                        call.respondText("Upload not successful")
                    }
                } else {
                    call.respond(HttpStatusCode.NotAcceptable, "invalid order ID")
                }
            }
            part.dispose()
        }
    }
}

fun Route.updateBasicSettingsRoute(objectRepository: ObjectRepository) {
    post("/order/{id}/basic") {
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
    post("/order/{id}/advanced") {
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
