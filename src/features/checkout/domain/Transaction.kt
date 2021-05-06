package com.example.features.checkout.domain

import com.example.features.order.domain.Order

data class Transaction(
    val userId: String,
    val orders: ArrayList<Order>
)
