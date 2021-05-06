package com.example.features.admin.data

import com.example.features.order.domain.Object

class AdminRepository(
    private val adminDataSource: AdminDataSource
) {
    suspend fun getProcessingOrders(): ArrayList<Object> {
        return adminDataSource.getProcessingOrders()
    }

    suspend fun getOrderHistory(): ArrayList<Object> {
        return adminDataSource.getOrderHistory()
    }

    suspend fun getProcessingOrder(orderId: String): Object? {
        return adminDataSource.getProcessingOrder(orderId)
    }

    suspend fun updateProcessingOrder(order: Object): Boolean {
        return adminDataSource.updateProcessingOrder(order)
    }

    suspend fun orderDelivered(orderId: String): Boolean {
        return adminDataSource.orderDelivered(orderId)
    }
}