package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class Slicing(
    val _super: SlicingDetails = SlicingDetails(),
    val dynamic: SlicingDetails = SlicingDetails(),
    val standard: SlicingDetails = SlicingDetails(),
    val low: SlicingDetails = SlicingDetails(),
    val custom: SlicingDetails = SlicingDetails(),
)

@Serializable
data class SlicingDetails(
    val printTime: String? = null,
    val materialWeight: Float? = null,
    val filament: Float? = null,
    val materialCost: Float? = null,
    val powerCost: Float? = null,
    val labourCost: Float? = null,
    val price: Int? = null
)