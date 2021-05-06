package features.cart

import com.example.features.account.domain.User
import com.example.features.cart.data.CartDataSource
import com.example.features.order.domain.Object

class FakeCartDataSourceImpl(
    private val userData: HashMap<String, User>,
    private val wishlistOrders: HashMap<String, Object>,
    private val cartOrders: HashMap<String, Object>
) : CartDataSource {

    override suspend fun getUserCartOrders(email: String): ArrayList<Object> {
        val user = userData[email]!!
        return ArrayList(
            user.cartOrders.map {
                cartOrders[it]!!
            }
        )
    }

    override suspend fun removeCartOrder(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        val removed = user.cartOrders.remove(orderId)
        if (removed) {
            user.wishlist.add(orderId)
            userData[email] = user

            val order = cartOrders[orderId]!!
            cartOrders.remove(orderId)
            wishlistOrders[orderId] = order

            return true
        }
        return false
    }

    override suspend fun getAllCartOrders(): ArrayList<Object> {
        return ArrayList(cartOrders.values)
    }
}