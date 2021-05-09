package fakeDataSource

import com.example.database.order.OrderDataSource
import com.example.features.checkout.domain.Order
import com.example.features.checkout.domain.OrderStatus
import com.example.features.checkout.domain.OrderStatus.*
import data.Constants.TEST_CREATED_ORDER

class FakeOrderDataSourceImpl(
    private val orders: HashMap<String, Order>
): OrderDataSource {

    override suspend fun insertOrder(order: Order): Boolean {
        val testOrder = order.copy(id = TEST_CREATED_ORDER)
        orders[testOrder.id] = testOrder
        return true
    }

    override suspend fun getOrder(orderId: String): Order? {
        return orders[orderId]
    }

    override suspend fun updateTrackingStatus(orderId: String, status: OrderStatus): Boolean {
        val order = orders[orderId]?.copy(status = status) ?: return false
        orders[orderId] = order
        return true
    }

    override suspend fun getAllActiveOrders(): ArrayList<Order> {
        return ArrayList(orders.values.filter { it.status != DELIVERED })
    }

    override suspend fun getAllCompletedOrders(): ArrayList<Order> {
        return ArrayList(orders.values.filter { it.status == DELIVERED })
    }
}