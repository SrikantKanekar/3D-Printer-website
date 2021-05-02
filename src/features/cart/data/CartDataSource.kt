package com.example.features.cart.data

import com.example.features.order.domain.Order

interface CartDataSource {

    suspend fun getUserCartOrders(email: String): ArrayList<Order>

    suspend fun removeCartOrder(email: String, orderId: String): Boolean
}