package com.example.database.order

import com.example.model.Order
import com.example.util.enums.OrderStatus

interface OrderDataSource {

    suspend fun generateNewOrder(id: String, userEmail: String): Order

    suspend fun insertOrder(order: Order)

    suspend fun updateOrder(order: Order)

    suspend fun getOrderById(id: String): Order?

    suspend fun getOrdersByUser(userEmail: String): List<Order>

    suspend fun updateOrderStatus(id: String, status: OrderStatus)

    suspend fun updateOrderDelivery(id: String, date: String)

    suspend fun getAllActiveOrders(): List<Order>
}