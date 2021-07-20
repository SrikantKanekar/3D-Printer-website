package com.example.features.checkout.requests

import kotlinx.serialization.Serializable

@Serializable
data class CheckoutProceedRequest(
    val success: Boolean
)