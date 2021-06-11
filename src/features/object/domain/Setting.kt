package com.example.features.`object`.domain

import com.example.features.`object`.domain.PrintSequence.*
import com.example.features.`object`.domain.Quality.*
import com.example.features.`object`.domain.SupportPattern.*
import com.example.features.`object`.domain.SupportPlacement.*
import com.example.features.`object`.domain.SupportStructure.*
import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val advanced: Boolean = false,

    //basic
    val quality: Quality = STANDARD,
    val infill: Float = 20F,
    val gradualInfill: Boolean = false,
    val support: Boolean = true,

    // intermediate
    val layerHeight: Float = 0.2F,
    val infillDensity: Float = 20F,
    val infillPattern: InfillPattern = InfillPattern.LINES,
    val generateSupport: Boolean = true,
    val supportStructure: SupportStructure = NORMAL,
    val supportPlacement: SupportPlacement = TOUCHING_BUILD_PLATE,
    val supportOverhangAngle: Float = 45F,
    val supportPattern: SupportPattern = GRID,
    val supportDensity: Float = 20F,

    // advanced
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
    val printSequence: PrintSequence = ALL_AT_ONCE
)

enum class Quality {
    SUPER, DYNAMIC, STANDARD, LOW
}

enum class InfillPattern {
    LINES, GRID, TRIANGLES, TRI_HEXAGON, CUBIC, CUBIC_SUBDIVISION,
    OCTET, QUARTER_CUBIC, CONCENTRIC, ZIG_ZAG, CROSS, CROSS_3D, GYROID
}

enum class SupportStructure {
    NORMAL, TREE
}

enum class SupportPlacement {
    TOUCHING_BUILD_PLATE, EVERYWHERE
}

enum class SupportPattern {
    LINES, GRID, TRIANGLES, CONCENTRIC, ZIG_ZAG, CROSS, GYROID
}

enum class PrintSequence {
    ALL_AT_ONCE, ONE_AT_A_TIME
}
