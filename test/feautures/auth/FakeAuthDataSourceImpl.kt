package feautures.auth

import com.example.feautures.wishlist.domain.WishlistCookie
import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.account.domain.User

class FakeAuthDataSourceImpl(
    private val userData: HashMap<String, User>
) : AuthDataSource {

    override suspend fun insert(user: User): Boolean {
        userData[user.email] = user
        return true
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return userData.containsKey(email)
    }

    override suspend fun getPassword(email: String): String? {
        return userData[email]?.password
    }

    override suspend fun syncOrders(email: String, wishlistCookie: WishlistCookie?): Boolean {
        wishlistCookie?.let {
            val user = userData[email]!!
            wishlistCookie.orders.forEach { order ->
                if (!user.wishlist.contains(order)) user.wishlist.add(order)
            }
            userData[email] = user
        }
        return true
    }
}