package com.example.features.notification.domain

object EmailNotification {

    fun send(notification: Notification, email: String): Boolean {
        println("---------------Email Notification-------------")
        println("To: $email")
        println("Title: ${notification.title}")
        println("message: ${notification.message}")
        println("posted at: ${notification.posted_at}")
        return true
    }
}