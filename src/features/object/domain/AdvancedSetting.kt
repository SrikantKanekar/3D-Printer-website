package com.example.features.`object`.domain

import com.example.features.`object`.domain.PrintSequence.*
import kotlinx.serialization.Serializable

@Serializable
data class AdvancedSetting(
    val wallLineWidth: Float = 0.4F,
    val topBottomLineWidth: Float = 0.4F,
    val wallThickness: Float = 0.8F,
    val wallLineCount: Int = 2,
    val topThickness: Float = 1.12F,
    val bottomThickness: Float = 1.12F,
    val infillSpeed: Float = 50F,
    val outerWallSpeed: Float = 25F,
    val innerWallSpeed: Float = 25F,
    val topBottomSpeed: Float = 25F,
    val supportSpeed: Float = 25F,
    val printSequence: PrintSequence = ALL_AT_ONCE,
)

enum class PrintSequence{
    ALL_AT_ONCE, ONE_AT_A_TIME
}