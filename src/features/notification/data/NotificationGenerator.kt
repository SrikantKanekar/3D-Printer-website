package com.example.features.notification.data

import com.example.model.Notification
import com.example.model.Object
import com.example.model.Order
import com.example.model.User
import com.example.util.enums.NotificationType
import com.example.util.enums.NotificationType.*

fun generateNotification(
    type: NotificationType,
    user: User,
    order: Order,
    objectId: String? = null
): Notification {
    return when (type) {
        PLACED -> Notification(
            subject = "Order Placed",
            body = placedMessage(order)
        )
        CONFIRMED -> Notification(
            subject = "Order Confirmed",
            body = confirmedMessage(order)
        )
        PRINTING -> {
            val obj = user.objects.find { it.id == objectId }!!
            Notification(
                subject = "Object printing Started",
                body = printingMessage(order, obj)
            )
        }
        DELIVERING -> Notification(
            subject = "Out for delivery",
            body = deliveringMessage(order)
        )
        DELIVERED -> Notification(
            subject = "successfully delivered",
            body = deliveredMessage(order)
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