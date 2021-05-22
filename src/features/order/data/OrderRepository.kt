package com.example.features.order.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.notification.data.NotificationRepository
import com.example.features.notification.domain.NotificationType
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.*

class OrderRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
    private val notificationRepository: NotificationRepository
) {

    suspend fun getOrderForAdmin(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)
    }

    /**
     * Return order details only if it belongs to the user
     */
    suspend fun getOrderForUser(email: String, orderId: String): Order? {
        val user = userDataSource.getUser(email)
        return if (user.orderIds.contains(orderId)) {
            orderDataSource.getOrderById(orderId)
        } else {
            null
        }
    }

    /**
     * get all objects present in current order
     */
    suspend fun getUserOrderObjects(order: Order): List<Object> {
        val user = userDataSource.getUser(order.userEmail)
        return user.objects.filter { order.objectIds.contains(it.id) }
    }

    /**
     * Update individual objects printing status by the admin
     */
    suspend fun updatePrintingStatus(orderId: String, objectId: String, printingStatus: PrintingStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false

        // The order must be in processing state
        if (order.status == PROCESSING) {

            // Update user object's printingStatus
            val user = userDataSource.getUser(order.userEmail)
            user.objects
                .filter { it.status == TRACKING }
                .filter { it.printingStatus.ordinal == printingStatus.ordinal - 1 }
                .find { it.id == objectId }
                ?.let { it.printingStatus = printingStatus } ?: return false
            val updated = userDataSource.updateUser(user)

            // sends notification after order completion
            if (updated && printingStatus == PRINTING) {
                notificationRepository.sendNotification(NotificationType.PRINTING, user, order, objectId)
            }
            return updated
        }
        return false
    }

    /**
     * Custom Notification can be sent by admin to the user
     */
    suspend fun sendCustomNotification(email: String, title: String, message: String): Boolean {
        val user = userDataSource.getUser(email)
        return notificationRepository.sendCustomNotification(user, title, message)
    }
}