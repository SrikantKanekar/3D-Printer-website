package com.example.features.admin.data

import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.*
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AdminDataSourceImpl(
    private val processingOrders: CoroutineCollection<Order>,
    private val historyOrders: CoroutineCollection<Order>
) : AdminDataSource {

    override suspend fun getProcessingOrders(): ArrayList<Order> {
        return ArrayList(processingOrders.find().toList())
    }

    override suspend fun getOrderHistory(): ArrayList<Order> {
        return ArrayList(historyOrders.find().toList())
    }

    override suspend fun getProcessingOrder(orderId: String): Order? {
        return processingOrders.findOneById(orderId)
    }

    override suspend fun updateProcessingOrder(order: Order): Boolean {
        return processingOrders.updateOne(Order::id eq order.id, order).wasAcknowledged()
    }

    // handle user as well(after adding userID to order object)
    override suspend fun orderDelivered(orderId: String): Boolean {
        val order = processingOrders.findOneById(orderId)
        return if (order != null) {
            val deleted = processingOrders.deleteOneById(order.id).wasAcknowledged()
            val inserted = historyOrders.insertOne(order.copy(status = DONE)).wasAcknowledged()
            deleted and inserted
        } else {
            false
        }
    }
}