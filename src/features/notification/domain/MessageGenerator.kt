package com.example.features.notification.domain

import com.example.features.`object`.domain.Object
import com.example.features.order.domain.Order

object MessageGenerator {

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
                "your object ${obj.filename} printing has started\n"
    }

    fun deliveringMessage(order: Order): String {
        return "Hello ${order.userEmail}\n" +
                "your order is out for delivery"
    }

    fun deliveredMessage(order: Order): String {
        return "Hello ${order.userEmail}\n" +
                "your order is delivered"
    }
}