package com.example.features.order.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*
import com.example.features.order.domain.PrintingStatus

class OrderRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource
) {

    suspend fun getOrderForAdmin(orderId: String): Order? {
        return orderDataSource.getOrderById(orderId)
    }

    suspend fun getOrderForUser(email: String, orderId: String): Order? {
        val user = userDataSource.getUser(email)
        return if (user.orderIds.contains(orderId)) {
            orderDataSource.getOrderById(orderId)
        } else {
            null
        }
    }

    suspend fun updatePrintingStatus(orderId: String, objectId: String, printingStatus: PrintingStatus): Boolean {
        val order = orderDataSource.getOrderById(orderId) ?: return false
        if (order.status == PROCESSING){
            val user = userDataSource.getUser(order.userEmail)
            user.objects
                .filter { it.status == TRACKING }
                .filter { it.printingStatus.ordinal == printingStatus.ordinal - 1 }
                .find { it.id == objectId }
                ?.let { it.printingStatus = printingStatus } ?: return false
            return userDataSource.updateUser(user)
        }
        return false
    }
}