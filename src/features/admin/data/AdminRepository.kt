package com.example.features.admin.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.notification.data.NotificationRepository
import com.example.features.notification.domain.NotificationType
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.*
import com.example.features.order.domain.PrintingStatus.*

class AdminRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
    private val notificationRepository: NotificationRepository
) {
    suspend fun getAllActiveOrders(): ArrayList<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun getAllCompletedOrders(): ArrayList<Order> {
        return orderDataSource.getAllCompletedOrders()
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

        // send notification to user when delivery starts
        if (updated) {
            if (status == CONFIRMED){
                notificationRepository.sendNotification(NotificationType.CONFIRMED, user, order)
            } else if (status == DELIVERING) {
                notificationRepository.sendNotification(NotificationType.DELIVERING, user, order)
            }
        }

        // update status of all individual objects and send notification
        if (status == DELIVERED && updated) {
            user.objects
                .filter { it.status == TRACKING }
                .filter { order.objectIds.contains(it.id) }
                .forEach { it.status = COMPLETED }
            notificationRepository.sendNotification(NotificationType.DELIVERED, user, order)
            return userDataSource.updateUser(user)
        }
        return updated
    }
}