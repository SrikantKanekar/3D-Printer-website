package com.example.features.order.presentation

import com.example.features.order.data.OrderRepository
import com.example.features.order.domain.PrintingStatus
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updatePrintingStatus(objectRepository: OrderRepository) {
    post("/order/update/printing-status") {

        val params = call.receiveParameters()
        val orderId = params["orderId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val objectId = params["objectId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
        val status = params["printing_status"]?.toInt() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val printingStatus = PrintingStatus.values()[status]

        val updated = objectRepository.updatePrintingStatus(orderId, objectId, printingStatus)
        call.respond(updated)
    }
}