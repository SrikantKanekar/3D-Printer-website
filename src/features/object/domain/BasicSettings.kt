package com.example.features.`object`.domain

import kotlinx.serialization.Serializable

@Serializable
data class BasicSettings(
    val size: Int = 10
)