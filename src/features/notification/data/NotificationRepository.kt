package com.example.features.notification.data

import com.example.database.user.UserDataSource
import com.example.features.account.domain.User
import com.example.features.notification.domain.EmailNotification
import com.example.features.notification.domain.MessageGenerator
import com.example.features.notification.domain.Notification
import com.example.features.notification.domain.NotificationType
import com.example.features.notification.domain.NotificationType.*
import com.example.features.order.domain.Order

class NotificationRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getAllNotifications(email: String): List<Notification> {
        val user = userDataSource.getUser(email)
        return user.notification
    }

    suspend fun getNotificationById(email: String, id: String): Notification? {
        val user = userDataSource.getUser(email)
        return user.notification.find { it.id == id }
    }

    suspend fun sendNotification(
        type: NotificationType,
        user: User,
        order: Order,
        objectId: String? = null
    ) {
        val notification = when (type) {
            PLACED -> Notification(
                title = "Order Confirmed",
                message = MessageGenerator.placedMessage(order)
            )
            PRINTING -> {
                val obj = user.objects.find { it.id == objectId }!!
                Notification(
                    title = "Object printing Started",
                    message = MessageGenerator.printingMessage(order, obj)
                )
            }
            DELIVERING -> Notification(
                title = "Out for delivery",
                message = MessageGenerator.deliveringMessage(order)
            )
            DELIVERED -> Notification(
                title = "successfully delivered",
                message = MessageGenerator.deliveredMessage(order)
            )
        }
        EmailNotification.send(notification, user.email)
        user.notification.add(notification)
        userDataSource.updateUser(user)
    }

    suspend fun sendCustomNotification(
        user: User,
        title: String,
        message: String
    ): Boolean {
        val notification = Notification(
            title = title,
            message = message
        )
        val result = EmailNotification.send(notification, user.email)
        if (result) {
            user.notification.add(notification)
            return userDataSource.updateUser(user)
        }
        return false
    }
}
