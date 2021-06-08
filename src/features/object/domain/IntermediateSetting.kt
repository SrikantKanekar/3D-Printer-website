package com.example.features.`object`.domain

import com.example.features.`object`.domain.InfillPattern.*
import com.example.features.`object`.domain.SupportPlacement.*
import com.example.features.`object`.domain.SupportStructure.*
import kotlinx.serialization.Serializable

@Serializable
data class IntermediateSetting(
    val layerHeight: Float = 0.2F,
    val infillDensity: Float = 20F,
    val infillPattern: InfillPattern = LINES,
    val generateSupport: Boolean = true,
    val supportStructure: SupportStructure = NORMAL,
    val supportPlacement: SupportPlacement = TOUCHING_BUILD_PLATE,
    val supportOverhangAngle: Float = 45F,
    val supportPattern: SupportPattern = SupportPattern.GRID,
    val supportDensity: Float = 20F
)

enum class InfillPattern{
    LINES, GRID, TRIANGLES, TRI_HEXAGON, CUBIC, CUBIC_SUBDIVISION,
    OCTET, QUARTER_CUBIC, CONCENTRIC, ZIG_ZAG, CROSS, CROSS_3D, GYROID
}

enum class SupportStructure{
    NORMAL, TREE
}

enum class SupportPlacement{
    TOUCHING_BUILD_PLATE, EVERYWHERE
}

enum class SupportPattern{
    LINES, GRID, TRIANGLES, CONCENTRIC, ZIG_ZAG, CROSS, GYROID
}