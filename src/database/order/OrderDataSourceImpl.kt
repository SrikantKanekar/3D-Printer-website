package com.example.database.order

import com.example.features.checkout.domain.Order
import com.example.features.checkout.domain.OrderStatus
import com.example.features.checkout.domain.OrderStatus.DELIVERED
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.setValue

class OrderDataSourceImpl(
    private val orders: CoroutineCollection<Order>
) : OrderDataSource {

    override suspend fun insertOrder(order: Order): Boolean {
        return orders.insertOne(order).wasAcknowledged()
    }

    override suspend fun getOrder(orderId: String): Order? {
        return orders.findOne(Order::id eq orderId)
    }

    override suspend fun updateTrackingStatus(orderId: String, status: OrderStatus): Boolean {
        return orders
            .updateOne(Order::id eq orderId, setValue(Order::status, status))
            .wasAcknowledged()
    }

    override suspend fun getAllActiveOrders(): ArrayList<Order> {
        return ArrayList(orders.find().filter(Order::status ne DELIVERED).toList())
    }

    override suspend fun getAllCompletedOrders(): ArrayList<Order> {
        return ArrayList(orders.find().filter(Order::status eq DELIVERED).toList())
    }
}