package com.example.database.order

import com.example.model.Order
import com.example.util.enums.OrderStatus

interface OrderDataSource {

    suspend fun generateNewOrder(userEmail: String): Order

    suspend fun insertOrder(order: Order)

    suspend fun getOrderById(orderId: String): Order?

    suspend fun getOrdersByUser(userEmail: String): List<Order>

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus)

    suspend fun updateOrderDelivery(orderId: String, date: String)

    suspend fun getAllActiveOrders(): List<Order>
}