package com.example.features.`object`.domain

import com.example.features.`object`.domain.Quality.*
import kotlinx.serialization.Serializable

@Serializable
data class BasicSetting(
    val quality: Quality = STANDARD,
    val infill: Float = 20F,
    val gradualInfill: Boolean = false,
    val support: Boolean = true
)

enum class Quality{
    SUPER, DYNAMIC, STANDARD, LOW
}
