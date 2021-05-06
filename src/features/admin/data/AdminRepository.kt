package com.example.features.admin.data

import com.example.features.order.domain.Order

class AdminRepository(
    private val adminDataSource: AdminDataSource
) {
    suspend fun getProcessingOrders(): ArrayList<Order> {
        return adminDataSource.getProcessingOrders()
    }

    suspend fun getOrderHistory(): ArrayList<Order> {
        return adminDataSource.getOrderHistory()
    }

    suspend fun getProcessingOrder(orderId: String): Order? {
        return adminDataSource.getProcessingOrder(orderId)
    }

    suspend fun updateProcessingOrder(order: Order): Boolean {
        return adminDataSource.updateProcessingOrder(order)
    }

    suspend fun orderDelivered(orderId: String): Boolean {
        return adminDataSource.orderDelivered(orderId)
    }
}