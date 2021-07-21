package com.example.features.order.response

import com.example.model.Object
import com.example.model.Order
import kotlinx.serialization.Serializable

@Serializable
data class OrderObjectResponse(
    val order: Order,
    val objects: List<Object>
)