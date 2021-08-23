package com.example.util

import com.example.features.admin.requests.SpecialFulfillRequest
import com.example.model.SlicingDetails
import kotlin.math.roundToLong

/**
 * time in min
 * material in grams
 */
fun calculateSlicingDetails(request: SpecialFulfillRequest): SlicingDetails {
    val (time, material) = request

    val hours = (time / 60).toInt()
    val minutes = (time % 60).toInt()

    val printTime = if(hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
    val materialWeight = material.toTwoDecimal()
    val filament = (material / 2.958).toTwoDecimal()
    val materialCost = (materialWeight * 1.85).toTwoDecimal()
    val powerCost = (time / 60 * 2).toTwoDecimal()
    val labourCost = (time / 60 * 10).toTwoDecimal()
    val price = (materialCost + powerCost + labourCost).toInt()

    return SlicingDetails(
        printTime,
        materialWeight,
        filament,
        materialCost,
        powerCost,
        labourCost,
        price
    )
}

fun Float.toTwoDecimal() = ((this * 100.0).roundToLong() / 100.0).toFloat()
fun Double.toTwoDecimal() = ((this * 100.0).roundToLong() / 100.0).toFloat()