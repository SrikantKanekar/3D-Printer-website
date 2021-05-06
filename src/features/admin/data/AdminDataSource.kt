package com.example.features.admin.data

import com.example.features.order.domain.Order

interface AdminDataSource {

    suspend fun getProcessingOrders(): ArrayList<Order>

    suspend fun getOrderHistory(): ArrayList<Order>

    suspend fun getProcessingOrder(orderId: String): Order?

    suspend fun updateProcessingOrder(order: Order): Boolean

    suspend fun orderDelivered(orderId: String): Boolean
}