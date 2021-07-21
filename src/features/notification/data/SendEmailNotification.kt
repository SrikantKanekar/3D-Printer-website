package com.example.features.notification.data

import com.example.config.AppConfig
import com.example.model.Notification
import org.apache.commons.mail.DefaultAuthenticator
import org.apache.commons.mail.SimpleEmail

fun sendEmailNotification(
    notification: Notification,
    userEmail: String,
    appConfig: AppConfig
) {
    when (appConfig.testing) {
        true -> {
            println("---------------Email Notification-------------")
            println("To: $userEmail")
            println("Title: ${notification.subject}")
            println("message: ${notification.body}")
            println("posted at: ${notification.posted_at}")
        }
        false -> {
            val auth = DefaultAuthenticator(
                appConfig.notificationConfig.email,
                appConfig.notificationConfig.password
            )
            val email = SimpleEmail()
            email.hostName = "smtp.gmail.com"
            email.setSmtpPort(465)
            email.setAuthenticator(auth)
            email.isSSLOnConnect = true
            email.setFrom(appConfig.notificationConfig.email)

            email.addTo(userEmail)
            email.subject = notification.subject
            email.setMsg(notification.body)

            email.send()
        }
    }
}