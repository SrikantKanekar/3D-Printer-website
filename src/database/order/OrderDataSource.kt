package com.example.database.order

import com.example.features.checkout.domain.Order
import com.example.features.checkout.domain.OrderStatus

interface OrderDataSource {

    suspend fun insertOrder(order: Order): Boolean

    suspend fun getOrder(orderId: String): Order?

    suspend fun updateTrackingStatus(orderId: String, status: OrderStatus): Boolean

    suspend fun getAllActiveOrders(): ArrayList<Order>

    suspend fun getAllCompletedOrders(): ArrayList<Order>
}