package com.example.features.admin.routes

import com.example.features.admin.data.AdminRepository
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.getAllActiveOrders(adminRepository: AdminRepository) {
    get {
        val activeOrders = adminRepository.getAllActiveOrders()
        call.respond(activeOrders)
    }
}