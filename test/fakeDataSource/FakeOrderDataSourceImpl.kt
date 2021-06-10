package fakeDataSource

import com.example.database.order.OrderDataSource
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus
import com.example.features.order.domain.OrderStatus.*
import data.Constants.TEST_CREATED_ORDER

class FakeOrderDataSourceImpl(
    private val orders: HashMap<String, Order>
): OrderDataSource {

    override suspend fun creteNewOrder(userEmail: String, price: Int, deliveryDays: Int): Order {
        return Order(
            id = TEST_CREATED_ORDER,
            userEmail = userEmail,
            price = price,
            deliveryDays = deliveryDays
        )
    }

    override suspend fun insertOrder(order: Order): Boolean {
        orders[order.id] = order
        return true
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders[orderId]
    }

    override suspend fun getOrdersByUser(userEmail: String): List<Order> {
        return orders.values.filter { it.userEmail == userEmail }
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orders[orderId]?.copy(status = status) ?: return false
        orders[orderId] = order
        return true
    }

    override suspend fun getAllActiveOrders(): List<Order> {
        return orders.values.filter { it.status != DELIVERED }
    }
}