package features.wishlist

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import com.example.features.wishlist.data.WishlistDataSource
import java.io.File

class FakeWishlistDataSourceImpl(
    private val userData: HashMap<String, User>,
    private val wishlistOrders: HashMap<String, Order>,
    private val cartOrders: HashMap<String, Order>
) : WishlistDataSource {

    override suspend fun getUserWishlist(email: String): ArrayList<String> {
        return userData[email]!!.wishlist
    }

    override suspend fun getWishlistOrderList(orderIds: ArrayList<String>): ArrayList<Order> {
        return ArrayList(
            orderIds.map {
                wishlistOrders[it]!!
            }
        )
    }

    override suspend fun deleteWishlist(orderId: String): Boolean {
        val deleted = File("uploads/$orderId").delete()
        wishlistOrders.remove(orderId)
        return deleted
    }

    override suspend fun deleteUserWishlist(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        val deleted = user.wishlist.remove(orderId)
        userData[email] = user
        return deleted
    }

    override suspend fun addToCart(email: String, orderId: String): Boolean {
        val user = userData[email]!!
        val removed = user.wishlist.remove(orderId)
        if (removed) {
            user.cartOrders.add(orderId)
            userData[email] = user

            val order = wishlistOrders[orderId]!!
            wishlistOrders.remove(orderId)
            cartOrders[orderId] = order
            return true
        }
        return false
    }

    override suspend fun getAllWishlistOrders(): ArrayList<Order> {
        return ArrayList(wishlistOrders.values)
    }
}