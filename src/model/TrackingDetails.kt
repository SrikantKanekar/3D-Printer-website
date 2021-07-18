package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class TrackingDetails(
    var started_at: String? = null,
    var completed_at: String? = null
)