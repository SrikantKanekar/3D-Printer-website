package com.example.features.notification.domain

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val message: String
)