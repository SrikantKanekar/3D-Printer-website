package com.example.features.admin.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.ObjectStatus.COMPLETED
import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.notification.domain.NotificationManager
import com.example.features.notification.domain.NotificationType
import com.example.features.notification.domain.generateNotification
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.*
import com.example.features.order.domain.PrintingStatus.PRINTED
import com.example.util.now

class AdminRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getAllActiveOrders(): List<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun getActiveOrder(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)?.takeUnless { it.status == DELIVERED }
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false
        val user = userDataSource.getUser(order.userEmail)

        // check if all objects are printed before delivering
        if (status == DELIVERING) {
            val allPrinted = user.objects
                .filter { it.status == TRACKING }
                .filter { order.objectIds.contains(it.id) }
                .all { it.printingStatus == PRINTED }
            if (!allPrinted) return false
        }

        // update order status
        val updated = if (order.status.ordinal == status.ordinal - 1) {
            orderDataSource.updateOrderStatus(orderId, status)
        } else false

        if (updated) {
            when (status) {
                CONFIRMED -> {
                    val notification = generateNotification(NotificationType.CONFIRMED, user, order)
                    NotificationManager.sendNotification(notification, user.email)
                    user.notification.add(notification)
                }
                DELIVERING -> {
                    val notification = generateNotification(NotificationType.DELIVERING, user, order)
                    NotificationManager.sendNotification(notification, user.email)
                    user.notification.add(notification)
                }
                DELIVERED -> {
                    user.objects
                        .filter { it.status == TRACKING }
                        .filter { order.objectIds.contains(it.id) }
                        .forEach { it.status = COMPLETED }
                    orderDataSource.updateOrderDelivery(orderId, now())

                    val notification = generateNotification(NotificationType.DELIVERED, user, order)
                    NotificationManager.sendNotification(notification, user.email)
                    user.notification.add(notification)
                }
                else -> {}
            }
            return userDataSource.updateUser(user)
        }
        return false
    }
}