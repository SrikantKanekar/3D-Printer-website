package com.example.database.order

import com.example.model.Order
import com.example.util.DatabaseException
import com.example.util.enums.OrderStatus
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import org.litote.kmongo.ne
import org.litote.kmongo.setValue

class OrderDataSourceImpl(
    private val orders: CoroutineCollection<Order>
) : OrderDataSource {

    override suspend fun generateNewOrder(userEmail: String): Order {
        return Order(userEmail = userEmail)
    }

    override suspend fun insertOrder(order: Order) {
        val inserted = orders.insertOne(order).wasAcknowledged()
        if (!inserted) throw DatabaseException(
            "error inserting order of ${order.userEmail}"
        )
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders.findOne(Order::id eq orderId)
    }

    override suspend fun getOrdersByUser(userEmail: String): List<Order> {
        return orders.find(Order::userEmail eq userEmail).toList().reversed()
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus) {
        val updated = orders
            .updateOne(Order::id eq orderId, setValue(Order::status, status))
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating order status $status of order $orderId"
        )
    }

    override suspend fun updateOrderDelivery(orderId: String, date: String) {
        val updated = orders
            .updateOne(Order::id eq orderId, setValue(Order::deliveredOn, date))
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating order delivery date $date of order $orderId"
        )
    }

    override suspend fun getAllActiveOrders(): List<Order> {
        return orders
            .find()
            .filter(Order::status ne OrderStatus.DELIVERED)
            .toList()
            .reversed()
    }
}