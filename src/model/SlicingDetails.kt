package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class SlicingDetails(
    var uptoDate: Boolean = false, // indicate weather slicing details are up to date
    var time: Long? = null, // approx printing time in milliseconds
    var materialWeight: Int? = null,
    var materialCost: Int? = null,
    var electricityCost: Int? = null,
    var totalPrice: Int? = null
)
