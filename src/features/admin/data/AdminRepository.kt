package com.example.features.admin.data

import com.example.database.order.OrderDataSource
import com.example.features.checkout.domain.Order
import com.example.features.checkout.domain.OrderStatus
import com.example.features.checkout.domain.OrderStatus.DELIVERED

class AdminRepository(
    private val orderDataSource: OrderDataSource
) {
    suspend fun getAllActiveOrders(): ArrayList<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun getAllCompletedOrders(): ArrayList<Order> {
        return orderDataSource.getAllCompletedOrders()
    }

    suspend fun getActiveOrder(orderId: String): Order? {
        return orderDataSource.getOrder(orderId)?.takeUnless { it.status == DELIVERED }
    }

    suspend fun updateTrackingStatus(orderId: String, status: OrderStatus): Boolean {
        return orderDataSource.updateTrackingStatus(orderId, status)
    }
}