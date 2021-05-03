package com.example.features.order.domain

import kotlinx.serialization.Serializable

@Serializable
data class BasicSettings(
    val size: Int = 10
)