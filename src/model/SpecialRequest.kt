package com.example.model

data class SpecialRequest(
    val _id: String,
    var fulfilled: Boolean = false,
    val userEmail: String,
    val fileUrl: String,
    val setting: Setting,
    var slicingDetails: SlicingDetails = SlicingDetails(),
    val requestedAt: String,
    var fulfilledAt: String? = null
)
