package com.example.features.`object`.domain

import kotlinx.serialization.Serializable

@Serializable
data class SlicingDetails(
    var uptoDate: Boolean = false, // indicate weather slicing details are up to date
    var time: String? = null, // approx printing time in minutes
    var materialWeight: Int? = null,
    var materialCost: Int? = null,
    var electricityCost: Int? = null,
    var totalPrice: Int? = null
)
