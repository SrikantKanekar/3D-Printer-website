package com.example.database.order

import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.DELIVERED
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.setValue

class OrderDataSourceImpl(
    private val orders: CoroutineCollection<Order>
) : OrderDataSource {

    override suspend fun creteNewOrder(userEmail: String, price: Int, deliveryDays: Int): Order {
        return Order(
            userEmail = userEmail,
            price = price,
            deliveryDays = deliveryDays
        )
    }

    override suspend fun insertOrder(order: Order): Boolean {
        return orders.insertOne(order).wasAcknowledged()
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders.findOne(Order::id eq orderId)
    }

    override suspend fun getOrdersByUser(userEmail: String): List<Order> {
        return orders.find(Order::userEmail eq userEmail).toList().reversed()
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        return orders
            .updateOne(Order::id eq orderId, setValue(Order::status, status))
            .wasAcknowledged()
    }

    override suspend fun getAllActiveOrders(): List<Order> {
        return orders.find().filter(Order::status ne DELIVERED).toList().reversed()
    }
}