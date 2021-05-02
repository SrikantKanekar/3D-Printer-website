package features.order

import com.example.features.account.domain.User
import com.example.features.order.data.OrderDataSource
import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Order
import data.Constants.TEST_CREATED_ORDER

class FakeOrderDataSourceImpl(
    private val userData: HashMap<String, User>,
    private val wishlistOrders: HashMap<String, Order>,
    private val cartOrders: HashMap<String, Order>
): OrderDataSource {

    override suspend fun createOrder(fileName: String): Order {
        // replace auto-generated id with test id
        val testOrder = Order(id = TEST_CREATED_ORDER, fileName = fileName)
        wishlistOrders[testOrder.id] = testOrder
        return testOrder
    }

    override suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        user.wishlist.add(orderId)
        userData[email] = user
        return true
    }

    private fun getOrderCollection(orderId: String): HashMap<String, Order>? {
        val wishlist = wishlistOrders[orderId]
        return if (wishlist == null) {
            val cart = cartOrders[orderId]
            if (cart != null) {
                cartOrders
            } else null
        } else wishlistOrders
    }

    override suspend fun getOrder(id: String): Order? {
        return getOrderCollection(id)?.get(id)
    }

    override suspend fun updateFileName(id: String, fileName: String): Boolean {
        val order = getOrderCollection(id)?.get(id)
        return when(order){
            null -> false
            else -> {
                getOrderCollection(id)?.set(id, order.copy(fileName = fileName))
                true
            }
        }
    }

    override suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean {
        val order = getOrderCollection(id)?.get(id)
        return when(order){
            null -> false
            else -> {
                getOrderCollection(id)?.set(id, order.copy(basicSettings = basicSettings))
                true
            }
        }
    }

    override suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean {
        val order = getOrderCollection(id)?.get(id)
        return when(order){
            null -> false
            else -> {
                getOrderCollection(id)?.set(id, order.copy(advancedSettings = advancedSettings))
                true
            }
        }
    }
}