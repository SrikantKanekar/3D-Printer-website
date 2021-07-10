package com.example.features.notification.data

import com.example.database.user.UserDataSource
import com.example.features.notification.domain.Notification

class NotificationRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getAllNotifications(email: String): List<Notification> {
        val user = userDataSource.getUser(email)
        return user.notification.reversed()
    }

    suspend fun getNotificationById(email: String, id: String): Notification? {
        val user = userDataSource.getUser(email)
        return user.notification.find { it.id == id }
    }
}
