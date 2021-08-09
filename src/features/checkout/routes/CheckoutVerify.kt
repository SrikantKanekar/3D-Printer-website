package com.example.features.checkout.routes

import com.example.config.AppConfig
import com.example.features.checkout.data.CheckoutRepository
import com.example.features.checkout.requests.RazorpayRequest
import com.example.features.notification.data.generateNotification
import com.example.features.notification.data.sendEmailNotification
import com.example.model.UserPrincipal
import com.example.util.enums.NotificationType.PLACED
import com.example.util.enums.ObjectStatus.TRACKING
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.checkoutVerify(repository: CheckoutRepository, appConfig: AppConfig) {
    post("/verify") {

        val body = call.receive<RazorpayRequest>()

        val verified = repository.verifySignature(body, appConfig)
        if (!verified) call.respond(HttpStatusCode.BadRequest, "Payment verfication failed")

        val order = repository.getOrderById(body.id)
            ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                "Order id is invalid"
            )

        val (email) = call.principal<UserPrincipal>()!!
        val user = repository.getUser(email)

        user.objects
            .filter { order.objectIds.contains(it.id) }
            .forEach { obj -> obj.status = TRACKING }
        user.orderIds.add(order._id)

        val notification = generateNotification(PLACED, user, order)
        sendEmailNotification(notification, user.email, appConfig)
        user.notification.add(notification)

        repository.updateUser(user)

        order.razorpay.order_id = body.order_id
        order.razorpay.payment_id = body.payment_id
        order.razorpay.signature = body.signature
        repository.updateOrder(order)

        call.respond(HttpStatusCode.OK, "Payment Successful")
    }
}