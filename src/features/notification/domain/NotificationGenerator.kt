package com.example.features.notification.domain

import com.example.features.`object`.domain.Object
import com.example.features.account.domain.User
import com.example.features.notification.domain.NotificationType.*
import com.example.features.order.domain.Order

fun generateNotification(
    type: NotificationType,
    user: User,
    order: Order,
    objectId: String? = null
): Notification {
    return when (type) {
        PLACED -> Notification(
            title = "Order Placed",
            message = placedMessage(order)
        )
        CONFIRMED -> Notification(
            title = "Order Confirmed",
            message = confirmedMessage(order)
        )
        PRINTING -> {
            val obj = user.objects.find { it.id == objectId }!!
            Notification(
                title = "Object printing Started",
                message = printingMessage(order, obj)
            )
        }
        DELIVERING -> Notification(
            title = "Out for delivery",
            message = deliveringMessage(order)
        )
        DELIVERED -> Notification(
            title = "successfully delivered",
            message = deliveredMessage(order)
        )
    }
}

fun placedMessage(order: Order): String {
    return "Hello ${order.userEmail}\n" +
            "your order is placed\n" +
            "${order.objectIds.size} items"
}

fun confirmedMessage(order: Order): String {
    return "Hello ${order.userEmail}\n" +
            "your order is confirmed\n" +
            "${order.objectIds.size} items"
}

fun printingMessage(order: Order, obj: Object): String {
    return "Hello ${order.userEmail}\n" +
            "your object ${obj.name} printing has started\n"
}

fun deliveringMessage(order: Order): String {
    return "Hello ${order.userEmail}\n" +
            "your order is out for delivery"
}

fun deliveredMessage(order: Order): String {
    return "Hello ${order.userEmail}\n" +
            "your order is delivered"
}