package com.example.features.order.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.notification.data.NotificationManager.sendNotification
import com.example.features.notification.data.generateNotification
import com.example.model.Notification
import com.example.model.Object
import com.example.model.Order
import com.example.util.enums.NotificationType
import com.example.util.enums.ObjectStatus.TRACKING
import com.example.util.enums.OrderStatus
import com.example.util.enums.PrintingStatus
import com.example.util.now

class OrderRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getUserOrders(email: String): List<Order> {
        return orderDataSource.getOrdersOfUser(email)
    }

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
     * * Update individual objects printing status by the admin
     * 1) Order must be in processing state
     * 2) Update Object's printing status and tracking details
     * 3) Send notification to user if Object printing is started.
     */
    suspend fun updatePrintingStatus(orderId: String, objectId: String, printingStatus: PrintingStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false

        // The order must be in processing state
        if (order.status == OrderStatus.PROCESSING) {

            val user = userDataSource.getUser(order.userEmail)

            // Update user object's printingStatus
            user.objects
                .filter { it.status == TRACKING }
                .filter { it.printingStatus.ordinal == printingStatus.ordinal - 1 }
                .find { it.id == objectId }
                ?.let { it.printingStatus = printingStatus } ?: return false

            // Update user object's tracking details
            if (printingStatus == PrintingStatus.PRINTING) {
                user.objects
                    .find { it.id == objectId }
                    ?.let { it.trackingDetails.started_at = now() }
            } else {
                user.objects
                    .find { it.id == objectId }
                    ?.let { it.trackingDetails.completed_at = now() }
            }


            // sends notification when printing starts
            if (printingStatus == PrintingStatus.PRINTING) {
                val notification = generateNotification(NotificationType.PRINTING, user, order, objectId)
                sendNotification(notification, user.email)
                user.notification.add(notification)
            }

            return userDataSource.updateUser(user)
        }
        return false
    }

    /**
     * Custom Notification can be sent by admin to the user
     */
    suspend fun sendCustomNotification(
        email: String,
        title: String,
        message: String
    ): Boolean {
        val notification = Notification(
            title = title,
            message = message
        )
        val result = sendNotification(notification, email)
        if (result){
            val user = userDataSource.getUser(email)
            user.notification.add(notification)
            return userDataSource.updateUser(user)
        }
        return false
    }
}