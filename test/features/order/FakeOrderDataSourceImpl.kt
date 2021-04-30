package features.order

import com.example.features.account.domain.User
import com.example.features.order.data.OrderDataSource
import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Order

class FakeOrderDataSourceImpl(
    private val wishlistOrders: HashMap<String, Order>,
    private val userData: HashMap<String, User>
): OrderDataSource {

    override suspend fun createOrder(order: Order): Boolean {
        wishlistOrders[order.id] = order
        return true
    }

    override suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        user.wishlist.add(orderId)
        userData[email] = user
        return true
    }

    override suspend fun getOrder(id: String): Order? {
        return wishlistOrders[id]
    }

    override suspend fun updateFileName(id: String, fileName: String): Boolean {
        val order = wishlistOrders[id]
        return when(order){
            null -> false
            else -> {
                wishlistOrders[id] = order.copy(fileName = fileName)
                true
            }
        }
    }

    override suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean {
        val order = wishlistOrders[id]
        return when(order){
            null -> false
            else -> {
                wishlistOrders[id] = order.copy(basicSettings = basicSettings)
                true
            }
        }
    }

    override suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean {
        val order = wishlistOrders[id]
        return when(order){
            null -> false
            else -> {
                wishlistOrders[id] = order.copy(advancedSettings = advancedSettings)
                true
            }
        }
    }
}