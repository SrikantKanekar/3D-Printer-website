package fakeDataSource

import com.example.database.order.OrderDataSource
import com.example.model.Order
import com.example.util.enums.OrderStatus
import data.TestConstants.TEST_CREATED_ORDER

class FakeOrderDataSourceImpl(
    private val orders: HashMap<String, Order>
) : OrderDataSource {

    override suspend fun generateNewOrder(id: String, userEmail: String): Order {
        return Order(
            _id = TEST_CREATED_ORDER,
            userEmail = userEmail
        )
    }

    override suspend fun insertOrder(order: Order) {
        orders[order._id] = order
    }

    override suspend fun updateOrder(order: Order) {
        orders[order._id] = order
    }

    override suspend fun getOrderById(id: String): Order? {
        return orders[id]
    }

    override suspend fun getOrdersByUser(userEmail: String): List<Order> {
        return orders.values
            .filter { it.userEmail == userEmail }
            .filter { it.razorpay.payment_id != null }
    }

    override suspend fun updateOrderStatus(id: String, status: OrderStatus) {
        val order = orders[id]!!.copy(status = status)
        orders[id] = order
    }

    override suspend fun updateOrderDelivery(id: String, date: String) {
        val order = orders[id]!!.copy(deliveredOn = date)
        orders[id] = order
    }

    override suspend fun getAllActiveOrders(): List<Order> {
        return orders.values
            .filter { it.razorpay.payment_id != null }
            .filter { it.status != OrderStatus.DELIVERED }
    }
}