package com.example.features.orders.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*

class OrdersRepository(
    private val orderDataSource: OrderDataSource
) {

    suspend fun getUserOrders(email: String): List<Order> {
        return orderDataSource.getOrdersByUser(email)
    }
}