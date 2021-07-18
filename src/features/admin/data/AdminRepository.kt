package com.example.features.admin.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.notification.data.NotificationManager
import com.example.features.notification.data.generateNotification
import com.example.model.Order
import com.example.util.enums.NotificationType
import com.example.util.enums.ObjectStatus.COMPLETED
import com.example.util.enums.ObjectStatus.TRACKING
import com.example.util.enums.OrderStatus
import com.example.util.enums.PrintingStatus
import com.example.util.now

class AdminRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getAllActiveOrders(): List<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun getActiveOrder(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)?.takeUnless { it.status == OrderStatus.DELIVERED }
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false
        val user = userDataSource.getUser(order.userEmail)

        // check if all objects are printed before delivering
        if (status == OrderStatus.DELIVERING) {
            val allPrinted = user.objects
                .filter { it.status == TRACKING }
                .filter { order.objectIds.contains(it.id) }
                .all { it.printingStatus == PrintingStatus.PRINTED }
            if (!allPrinted) return false
        }

        // update order status
        val updated = if (order.status.ordinal == status.ordinal - 1) {
            orderDataSource.updateOrderStatus(orderId, status)
        } else false

        if (updated) {
            when (status) {
                OrderStatus.CONFIRMED -> {
                    val notification = generateNotification(NotificationType.CONFIRMED, user, order)
                    NotificationManager.sendNotification(notification, user.email)
                    user.notification.add(notification)
                }
                OrderStatus.DELIVERING -> {
                    val notification = generateNotification(NotificationType.DELIVERING, user, order)
                    NotificationManager.sendNotification(notification, user.email)
                    user.notification.add(notification)
                }
                OrderStatus.DELIVERED -> {
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