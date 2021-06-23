package com.example.database.order

import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus

interface OrderDataSource {

    suspend fun creteNewOrder(userEmail: String): Order

    suspend fun insertOrder(order: Order): Boolean

    suspend fun getOrderById(orderId: String): Order?

    suspend fun getOrdersByUser(userEmail: String): List<Order>

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean

    suspend fun updateOrderDelivery(orderId: String, date: String): Boolean

    suspend fun getAllActiveOrders(): List<Order>
}