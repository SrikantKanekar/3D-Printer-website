package com.example.features.notification.data

import com.example.model.Notification
import kotlinx.coroutines.delay

object NotificationManager {

    suspend fun sendNotification(notification: Notification, email: String): Boolean {
        delay(1)
        println("---------------Email Notification-------------")
        println("To: $email")
        println("Title: ${notification.subject}")
        println("message: ${notification.body}")
        println("posted at: ${notification.posted_at}")
        return true
    }
}