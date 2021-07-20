package com.example.features.checkout.response

import com.example.model.Address
import com.example.model.Object
import kotlinx.serialization.Serializable

@Serializable
data class CheckoutResponse(
    val objects: List<Object>,
    val address: Address
)
