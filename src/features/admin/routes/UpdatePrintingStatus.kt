package com.example.features.order.presentation

import com.example.config.AppConfig
import com.example.features.admin.data.AdminRepository
import com.example.features.admin.requests.PrintingStatusRequest
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.updatePrintingStatus(adminRepository: AdminRepository, appConfig: AppConfig) {
    put("/printing-status") {
        val body = call.receive<PrintingStatusRequest>()
        val updated = adminRepository.updatePrintingStatus(body, appConfig)

        when (updated) {
            true -> call.respond(body.printingStatus)
            false -> call.respond(
                HttpStatusCode.MethodNotAllowed,
                "Order status not updated"
            )
        }
    }
}