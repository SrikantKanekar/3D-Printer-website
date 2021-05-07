package com.example.features.`object`.presentation

import com.example.features.myObjects.domain.ObjectsCookie
import com.example.features.auth.domain.UserIdPrincipal
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

fun Application.registerOrderRoutes() {

    val orderRepository by inject<ObjectRepository>()

    routing {
        getOrderRoute()
        createOrderRoute(orderRepository)
        getUpdateOrderRoute(orderRepository)
        updateFileRoute(orderRepository)
        updateBasicSettingsRoute(orderRepository)
        updateAdvancedSettingsRoute(orderRepository)
    }
}

fun Route.getOrderRoute() {
    get("/order") {
        val principal = call.sessions.get<UserIdPrincipal>()
        call.respond(FreeMarkerContent("order_create.ftl", mapOf("user" to (principal?.email ?: ""))))
    }
}

/**
1.Create a new order.

2.If user is logged in, save orderId in user database, else save in cookie.

3.Upload the file in 'uploads' folder with name orderId.
 **/
fun Route.createOrderRoute(objectRepository: ObjectRepository) {
    post("/order/create") {
        val multipartData = call.receiveMultipart()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                try {
                    val fileName = part.originalFileName!!
                    val order = objectRepository.createNewObject(fileName)

                    val file = File("uploads/${order.id}")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
                            val principal = call.sessions.get<UserIdPrincipal>()
                            if (principal != null) {
                                objectRepository.addUserObject(principal.email, order)
                            } else {
                                val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
                                cookie.objects.add(order)
                                call.sessions.set(cookie)
                            }
                            call.respondRedirect("/order/${order.id}")
                        }
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                    call.respondText("Upload not successful... retry")
                }
            }
            part.dispose()
        }
    }
}

fun Route.getUpdateOrderRoute(objectRepository: ObjectRepository) {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val principal = call.sessions.get<UserIdPrincipal>()
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
                try {
                    val fileName = part.originalFileName!!

                    val file = File("uploads/$id")
                    if (file.exists()) {
                        file.delete()
                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use { outputStream ->
                                its.copyTo(outputStream)
                                val principal = call.sessions.get<UserIdPrincipal>()
                                if (principal != null) {
                                    objectRepository.updateFileName(principal.email, id, fileName)
                                } else {
                                    val cookie = call.sessions.get<ObjectsCookie>()
                                    cookie?.objects?.find { it.id == id }?.fileName = fileName
                                    call.sessions.set(cookie)
                                }
                                call.respondText("Successfully updated")
                            }
                        }
                    } else {
                        call.respond(HttpStatusCode.NotAcceptable, "invalid order ID")
                    }
                } catch (e: Exception) {
                    println(e.localizedMessage)
                    call.respondText("Upload not successful... retry")
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

        val principal = call.sessions.get<UserIdPrincipal>()
        if (principal != null) {
            objectRepository.updateBasicSettings(principal.email, id, basicSettings)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>() ?: ObjectsCookie()
            cookie.objects.find { it.id == id }?.basicSettings = basicSettings
            call.sessions.set(cookie)
        }
        if (true) {
            call.respond(basicSettings)
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "invalid order ID")
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

        val principal = call.sessions.get<UserIdPrincipal>()
        if (principal != null) {
            objectRepository.updateAdvancedSettings(principal.email, id, advancedSettings)
        } else {
            val cookie = call.sessions.get<ObjectsCookie>()
            cookie?.objects?.find { it.id == id }?.advancedSettings = advancedSettings
            call.sessions.set(cookie)
        }
        if (true) {
            call.respond(advancedSettings)
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "invalid order ID")
        }
    }
}
