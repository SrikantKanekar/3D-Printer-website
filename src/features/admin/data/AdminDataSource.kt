package com.example.features.admin.data

import com.example.features.order.domain.Object

interface AdminDataSource {

    suspend fun getProcessingOrders(): ArrayList<Object>

    suspend fun getOrderHistory(): ArrayList<Object>

    suspend fun getProcessingOrder(orderId: String): Object?

    suspend fun updateProcessingOrder(order: Object): Boolean

    suspend fun orderDelivered(orderId: String): Boolean
}