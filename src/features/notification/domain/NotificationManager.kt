package com.example.features.notification.domain

import kotlinx.coroutines.delay

object NotificationManager {

    suspend fun sendNotification(notification: Notification, email: String): Boolean {
        delay(1)
        println("---------------Email Notification-------------")
        println("To: $email")
        println("Title: ${notification.title}")
        println("message: ${notification.message}")
        println("posted at: ${notification.posted_at}")
        return true
    }
}