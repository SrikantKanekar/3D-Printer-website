package com.example.features.`object`.domain

import kotlinx.serialization.Serializable

@Serializable
data class TrackingDetails(
    var started_at: String? = null,
    var completed_at: String? = null
)