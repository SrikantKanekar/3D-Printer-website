package fakeDataSource

import com.example.database.order.OrderDataSource
import com.example.model.Order
import com.example.util.enums.OrderStatus
import data.TestConstants.TEST_CREATED_ORDER

class FakeOrderDataSourceImpl(
    private val orders: HashMap<String, Order>
): OrderDataSource {

    override suspend fun creteNewOrder(userEmail: String): Order {
        return Order(
            id = TEST_CREATED_ORDER,
            userEmail = userEmail
        )
    }

    override suspend fun insertOrder(order: Order): Boolean {
        orders[order.id] = order
        return true
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orders[orderId]
    }

    override suspend fun getOrdersOfUser(userEmail: String): List<Order> {
        return orders.values.filter { it.userEmail == userEmail }
    }

    override suspend fun updateOrderStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orders[orderId]?.copy(status = status) ?: return false
        orders[orderId] = order
        return true
    }

    override suspend fun updateOrderDelivery(orderId: String, date: String): Boolean {
        val order = orders[orderId]?.copy(deliveredOn = date) ?: return false
        orders[orderId] = order
        return true
    }

    override suspend fun getAllActiveOrders(): List<Order> {
        return orders.values.filter { it.status != OrderStatus.DELIVERED }
    }
}