package features.checkout

import com.example.features.account.domain.User
import com.example.features.checkout.data.CheckoutDataSource
import com.example.features.checkout.domain.Address
import com.example.features.order.domain.Object
import com.example.features.order.domain.OrderStatus
import org.litote.kmongo.eq

class FakeCheckoutDataSourceImpl(
    private val userData: HashMap<String, User>,
    private val wishlistOrders: HashMap<String, Object>,
    private val cartOrders: HashMap<String, Object>
): CheckoutDataSource {

    override suspend fun getUserCartOrders(email: String): ArrayList<Object> {
        val user = userData[email]!!
        return ArrayList(
            user.cartOrders.map {
                cartOrders[it]!!
            }
        )
    }

    override suspend fun getUserAddress(email: String): Address {
        val user = userData[email]!!
        return user.address
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

    override suspend fun updateUserAddress(email: String, address: Address): Boolean {
        val user = userData[email]!!.copy(address = address)
        userData[email] = user
        return true
    }

    override suspend fun checkoutSuccess(email: String): Boolean {
        val user = userData[email]!!
        val cartOrdersIds = user.cartOrders
        user.currentOrders.addAll(cartOrdersIds)

        cartOrdersIds.forEach {
            val order = cartOrders[it]!!.copy(status = OrderStatus.PLACED)
            cartOrders.remove(it)
            //processingOrders.insertOne(order)
        }
        user.cartOrders.clear()
        userData[email] = user
        return true
    }
}