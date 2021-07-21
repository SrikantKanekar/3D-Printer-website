package com.example.features.order.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.model.Object
import com.example.model.Order
import com.example.model.UserPrincipal

class OrderRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource,
) {
    suspend fun getOrdersByUser(email: String): List<Order> {
        return orderDataSource.getOrdersByUser(email)
    }

    /**
     * Check if the order really belongs to that user
     */
    suspend fun getOrderForUserOrAdmin(
        principal: UserPrincipal,
        orderId: String
    ): Order? {
        return when (principal.isAdmin) {
            true -> orderDataSource.getOrderById(orderId)
            false -> {
                val user = userDataSource.getUser(principal.email)
                val hasOrder = user.orderIds.contains(orderId)
                when (hasOrder) {
                    true -> orderDataSource.getOrderById(orderId)
                    false -> null
                }
            }
        }
    }

    suspend fun getOrderObjects(order: Order): List<Object> {
        val user = userDataSource.getUser(order.userEmail)
        return user.objects.filter { order.objectIds.contains(it.id) }
    }
}