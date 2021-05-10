package com.example.features.admin.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.DELIVERED
import com.example.features.order.domain.OrderStatus.DELIVERING
import com.example.features.order.domain.PrintingStatus
import com.example.features.order.domain.PrintingStatus.*

class AdminRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource
) {
    suspend fun getAllActiveOrders(): ArrayList<Order> {
        return orderDataSource.getAllActiveOrders()
    }

    suspend fun getAllCompletedOrders(): ArrayList<Order> {
        return orderDataSource.getAllCompletedOrders()
    }

    suspend fun getActiveOrder(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)?.takeUnless { it.status == DELIVERED }
    }

    suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false

        val user = userDataSource.getUser(order.userEmail)
        val allPrinted = user.objects
            .filter { it.status == TRACKING }
            .filter { order.objectIds.contains(it.id) }
            .all { it.printingStatus == PRINTED }

        if (status == DELIVERING) if (!allPrinted) return false

        val updated = if (order.status.ordinal == status.ordinal - 1) {
            orderDataSource.updateOrderStatus(orderId, status)
        } else false

        if (status == DELIVERED && updated) {
            user.objects
                .filter { it.status == TRACKING }
                .filter { order.objectIds.contains(it.id) }
                .forEach { it.status = COMPLETED }
            return userDataSource.updateUser(user)
        }
        return updated
    }
}