package com.example.model

import com.example.model.PrintSequence.ALL_AT_ONCE
import com.example.model.SupportPattern.ZIG_ZAG
import com.example.model.SupportPlacement.EVERYWHERE
import com.example.model.SupportStructure.NORMAL
import com.example.util.enums.Quality
import com.example.util.enums.Quality.STANDARD
import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val quality: Quality = STANDARD,

    val layerHeight: Float = 0.2F,
    val infillDensity: Float = 20F,
    val gradualInfill: Boolean = false,
    val infillPattern: InfillPattern = InfillPattern.CUBIC,
    val generateSupport: Boolean = true,
    val supportStructure: SupportStructure = NORMAL,
    val supportPlacement: SupportPlacement = EVERYWHERE,
    val supportOverhangAngle: Float = 45F,
    val supportPattern: SupportPattern = ZIG_ZAG,
    val supportDensity: Float = 20F,

    val wallLineWidth: Float = 0.4F,
    val topBottomLineWidth: Float = 0.4F,
    val wallThickness: Float = 1.2F,
    val wallLineCount: Int = 3,
    val topThickness: Float = 0.8F,
    val bottomThickness: Float = 0.8F,
    val infillSpeed: Float = 50F,
    val outerWallSpeed: Float = 25F,
    val innerWallSpeed: Float = 25F,
    val topBottomSpeed: Float = 25F,
    val supportSpeed: Float = 25F,
    val printSequence: PrintSequence = ALL_AT_ONCE
)

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
