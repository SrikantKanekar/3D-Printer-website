package com.example.features.admin.requests

import kotlinx.serialization.Serializable

@Serializable
data class SpecialFulfillRequest(
    val time: Float,
    val material: Float
)
