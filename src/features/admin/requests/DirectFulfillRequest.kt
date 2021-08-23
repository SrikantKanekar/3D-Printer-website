package com.example.features.admin.requests

import kotlinx.serialization.Serializable

@Serializable
data class DirectFulfillRequest(
    val _super: SpecialFulfillRequest,
    val dynamic: SpecialFulfillRequest,
    val standard: SpecialFulfillRequest,
    val low: SpecialFulfillRequest,
)
