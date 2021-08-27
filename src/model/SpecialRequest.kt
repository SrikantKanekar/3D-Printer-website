package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class SpecialRequest(
    val _id: String,
    var fulfilled: Boolean = false,
    val userEmail: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String,
    val message: String,
    var slicingDetails: SlicingDetails = SlicingDetails(),
    val requestedAt: String,
    var fulfilledAt: String? = null
)
