package feautures.order

import com.example.feautures.account.domain.User
import com.example.feautures.order.data.OrderDataSource
import com.example.feautures.order.domain.AdvancedSettings
import com.example.feautures.order.domain.BasicSettings
import com.example.feautures.order.domain.Order

class FakeOrderDataSourceImpl(
    private val orderData: HashMap<String, Order>,
    private val userData: HashMap<String, User>
): OrderDataSource {

    override suspend fun createOrder(order: Order): Boolean {
        orderData[order.id] = order
        return true
    }

    override suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        user.wishlist.add(orderId)
        userData[email] = user
        return true
    }

    override suspend fun getOrder(id: String): Order? {
        return orderData[id]
    }

    override suspend fun updateFileName(id: String, fileName: String): Boolean {
        val order = orderData[id]
        return when(order){
            null -> false
            else -> {
                orderData[id] = order.copy(fileName = fileName)
                true
            }
        }
    }

    override suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean {
        val order = orderData[id]
        return when(order){
            null -> false
            else -> {
                orderData[id] = order.copy(basicSettings = basicSettings)
                true
            }
        }
    }

    override suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean {
        val order = orderData[id]
        return when(order){
            null -> false
            else -> {
                orderData[id] = order.copy(advancedSettings = advancedSettings)
                true
            }
        }
    }
}