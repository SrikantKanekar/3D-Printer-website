package com.example.database.order

import com.example.features.checkout.domain.Order

interface OrderDataSource {

    suspend fun insertOrder(order: Order): Boolean
}