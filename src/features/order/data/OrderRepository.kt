package com.example.features.order.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.TRACKING
import com.example.features.notification.domain.Notification
import com.example.features.notification.domain.NotificationManager.sendNotification
import com.example.features.notification.domain.NotificationType
import com.example.features.notification.domain.generateNotification
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.PROCESSING
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.PRINTING
import com.example.util.now

class OrderRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
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
     * * Update individual objects printing status by the admin
     * 1) Order must be in processing state
     * 2) Update Object's printing status and tracking details
     * 3) Send notification to user if Object printing is started.
     */
    suspend fun updatePrintingStatus(orderId: String, objectId: String, printingStatus: PrintingStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false

        // The order must be in processing state
        if (order.status == PROCESSING) {

            val user = userDataSource.getUser(order.userEmail)

            // Update user object's printingStatus
            user.objects
                .filter { it.status == TRACKING }
                .filter { it.printingStatus.ordinal == printingStatus.ordinal - 1 }
                .find { it.id == objectId }
                ?.let { it.printingStatus = printingStatus } ?: return false

            // Update user object's tracking details
            if (printingStatus == PRINTING) {
                user.objects
                    .find { it.id == objectId }
                    ?.let { it.trackingDetails.started_at = now() }
            } else {
                user.objects
                    .find { it.id == objectId }
                    ?.let { it.trackingDetails.completed_at = now() }
            }


            // sends notification when printing starts
            if (printingStatus == PRINTING) {
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