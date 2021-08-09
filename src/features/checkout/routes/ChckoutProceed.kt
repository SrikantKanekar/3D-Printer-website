package com.example.features.checkout.presentation

import com.example.config.AppConfig
import com.example.features.checkout.data.CheckoutRepository
import com.example.model.UserPrincipal
import com.example.util.enums.ObjectStatus.CART
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.checkoutProceed(
    repository: CheckoutRepository,
    appConfig: AppConfig
) {
    post("/proceed") {

        val (email) = call.principal<UserPrincipal>()!!
        val isNotEmpty = repository.isCartEmpty(email)

        if (isNotEmpty) {
            val user = repository.getUser(email)

            val price = repository.getOrderPrice(user)

            val orderId = repository.createRazorpayOrder(price, appConfig)
            val order = repository.generateNewOrder(id = orderId, email = email)
            order.price = price

            val orderIds = user.objects
                .filter { it.status == CART }
                .map { it.id }
            order.objectIds.addAll(orderIds)

            repository.insertOrder(order)
            call.respond(order)
        } else {
            call.respond(
                HttpStatusCode.MethodNotAllowed,
                "checkout is empty"
            )
        }
    }
}