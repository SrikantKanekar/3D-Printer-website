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

    override suspend fun generateNewOrder(id: String, userEmail: String): Order {
        return Order(_id = id, userEmail = userEmail)
    }

    override suspend fun insertOrder(order: Order) {
        val inserted = orders.insertOne(order).wasAcknowledged()
        if (!inserted) throw DatabaseException(
            "error inserting order of ${order.userEmail}"
        )
    }

    override suspend fun updateOrder(order: Order) {
        val updated = orders
            .updateOne(Order::_id eq order._id, order)
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating order of id ${order._id}"
        )
    }

    override suspend fun getOrderById(id: String): Order? {
        return orders.findOne(Order::_id eq id)
    }

    override suspend fun getOrdersByUser(userEmail: String): List<Order> {
        return orders.find(Order::userEmail eq userEmail).toList().reversed()
    }

    override suspend fun updateOrderStatus(id: String, status: OrderStatus) {
        val updated = orders
            .updateOne(Order::_id eq id, setValue(Order::status, status))
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating order status $status of order $id"
        )
    }

    override suspend fun updateOrderDelivery(id: String, date: String) {
        val updated = orders
            .updateOne(Order::_id eq id, setValue(Order::deliveredOn, date))
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating order delivery date $date of order $id"
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