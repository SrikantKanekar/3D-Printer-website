package com.example.features.admin.data

import com.example.config.AppConfig
import com.example.database.order.OrderDataSource
import com.example.database.request.DirectRequestDatasource
import com.example.database.request.SpecialRequestDatasource
import com.example.database.user.UserDataSource
import com.example.features.admin.requests.NotificationRequest
import com.example.features.admin.requests.PrintingStatusRequest
import com.example.features.notification.data.generateNotification
import com.example.features.notification.data.sendEmailNotification
import com.example.model.*
import com.example.util.enums.NotificationType
import com.example.util.enums.ObjectStatus.COMPLETED
import com.example.util.enums.ObjectStatus.TRACKING
import com.example.util.enums.OrderStatus
import com.example.util.enums.OrderStatus.*
import com.example.util.enums.PrintingStatus.PRINTED
import com.example.util.enums.PrintingStatus.PRINTING
import com.example.util.now

class AdminRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
    private val directRequestDatasource: DirectRequestDatasource,
    private val specialRequestDatasource: SpecialRequestDatasource
) {
    suspend fun getAllActiveOrders(): List<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun updateOrderStatus(
        orderId: String,
        status: OrderStatus,
        appConfig: AppConfig
    ): Boolean {
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
        when (order.status.ordinal == status.ordinal - 1) {
            true -> orderDataSource.updateOrderStatus(orderId, status)
            false -> return false
        }

        when (status) {
            CONFIRMED -> {
                val notification = generateNotification(NotificationType.CONFIRMED, user, order)
                sendEmailNotification(notification, user.email, appConfig)
                user.notification.add(notification)
            }
            DELIVERING -> {
                val notification = generateNotification(NotificationType.DELIVERING, user, order)
                sendEmailNotification(notification, user.email, appConfig)
                user.notification.add(notification)
            }
            DELIVERED -> {
                user.objects
                    .filter { it.status == TRACKING }
                    .filter { order.objectIds.contains(it.id) }
                    .forEach { it.status = COMPLETED }
                orderDataSource.updateOrderDelivery(orderId, now())

                val notification = generateNotification(NotificationType.DELIVERED, user, order)
                sendEmailNotification(notification, user.email, appConfig)
                user.notification.add(notification)
            }
            else -> {
            }
        }
        userDataSource.updateUser(user)
        return true
    }

    /**
     * * Update individual objects printing status by the admin
     * 1) Order must be in processing state
     * 2) Update Object's printing status and tracking details
     * 3) Send notification to user if Object printing is just started.
     */
    suspend fun updatePrintingStatus(
        request: PrintingStatusRequest,
        appConfig: AppConfig
    ): Boolean {
        val (orderId, objectId, printingStatus) = request

        val order = orderDataSource.getOrderById(orderId) ?: return false

        // The order must be in processing state
        val isProcessing = order.status == PROCESSING
        if (isProcessing) {
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
                sendEmailNotification(notification, user.email, appConfig)
                user.notification.add(notification)
            }
            userDataSource.updateUser(user)
        }
        return isProcessing
    }

    /**
     * Custom Notification can be sent by admin to the user
     */
    suspend fun sendNotification(
        request: NotificationRequest,
        appConfig: AppConfig
    ) {
        val notification = Notification(
            subject = request.subject,
            body = request.body
        )
        sendEmailNotification(notification, request.email, appConfig)

        val user = userDataSource.getUser(request.email)
        user.notification.add(notification)
        userDataSource.updateUser(user)
    }

    suspend fun getUser(email: String): User {
        return userDataSource.getUser(email)
    }

    suspend fun updateUser(user: User) {
        userDataSource.updateUser(user)
    }

    suspend fun getActiveDirectRequests(): List<DirectRequest> {
        return directRequestDatasource.getAllActive()
    }

    suspend fun getDirectRequestById(id: String): DirectRequest? {
        return directRequestDatasource.get(id)
    }

    suspend fun updateDirectRequest(request: DirectRequest) {
        directRequestDatasource.update(request)
    }

    suspend fun getActiveSpecialRequests(): List<SpecialRequest> {
        return specialRequestDatasource.getAllActive()
    }

    suspend fun getSpecialRequestById(id: String): SpecialRequest? {
        return specialRequestDatasource.get(id)
    }

    suspend fun updateSpecialRequest(request: SpecialRequest) {
        specialRequestDatasource.update(request)
    }
}