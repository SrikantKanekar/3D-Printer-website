package com.example.features.order.domain

import kotlinx.serialization.Serializable

@Serializable
data class AdvancedSettings(
    val weight: Int = 20
)