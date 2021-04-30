package com.example.feautures.order.presentation

import com.example.feautures.wishlist.domain.WishlistCookie
import com.example.feautures.auth.domain.UserIdPrincipal
import com.example.feautures.order.data.OrderRepository
import com.example.feautures.order.domain.AdvancedSettings
import com.example.feautures.order.domain.BasicSettings
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

    val orderRepository by inject<OrderRepository>()

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
        call.respond(FreeMarkerContent("order_create.ftl", null))
    }
}

/**
1.Create a new order.

2.If user is logged in, save orderId in user database, else save in cookie.

3.Upload the file in 'uploads' folder with name orderId.
 **/
fun Route.createOrderRoute(orderRepository: OrderRepository) {
    post("/order/create") {
        val multipartData = call.receiveMultipart()
        multipartData.forEachPart { part ->
            if (part is PartData.FileItem) {
                try {
                    val fileName = part.originalFileName!!
                    val order = orderRepository.createOrder(fileName)!!

                    val principal = call.sessions.get<UserIdPrincipal>()
                    if (principal != null) {
                        orderRepository.addOrderToUserWishlist(principal.email, order.id)
                    } else {
                        val cookie = call.sessions.get<WishlistCookie>() ?: WishlistCookie()
                        cookie.orders.add(order.id)
                        call.sessions.set(cookie)
                    }

                    val file = File("uploads/${order.id}")
                    part.streamProvider().use { its ->
                        file.outputStream().buffered().use {
                            its.copyTo(it)
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

fun Route.getUpdateOrderRoute(orderRepository: OrderRepository) {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val order = orderRepository.getOrder(id)
        if (order != null) {
            call.respond(FreeMarkerContent("order_update.ftl", mapOf("order" to order)))
        } else {
            call.respond(HttpStatusCode.NotAcceptable, "Invalid order ID")
        }
    }
}

fun Route.updateFileRoute(orderRepository: OrderRepository) {
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
                    val wasAcknowledged = orderRepository.updateFileName(id, fileName)

                    if (wasAcknowledged) {
                        val file = File("uploads/$id")
                        //delete old file with same id
                        file.delete()

                        //upload new file
                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use {
                                its.copyTo(it)
                                call.respondRedirect("/order/$id")
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

fun Route.updateBasicSettingsRoute(orderRepository: OrderRepository) {
    post("/order/{id}/basic") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val size = params["size"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val basicSettings = BasicSettings(size = size)
        val wasAcknowledged = orderRepository.updateBasicSettings(id, basicSettings)
        if (wasAcknowledged) {
            call.respondRedirect("/order/$id")
        } else {
            call.respond(HttpStatusCode.NotAcceptable,"invalid order ID")
        }
    }
}

fun Route.updateAdvancedSettingsRoute(orderRepository: OrderRepository) {
    post("/order/{id}/advanced") {
        val id = call.parameters["id"] ?: return@post call.respondText(
            text = "Missing or malformed id",
            status = HttpStatusCode.BadRequest
        )
        val params = call.receiveParameters()
        val weight = params["weight"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val advancedSettings = AdvancedSettings(weight = weight)
        val wasAcknowledged = orderRepository.updateAdvancedSettings(id, advancedSettings)
        if (wasAcknowledged) {
            call.respondRedirect("/order/$id")
        } else {
            call.respond(HttpStatusCode.NotAcceptable,"invalid order ID")
        }
    }
}
