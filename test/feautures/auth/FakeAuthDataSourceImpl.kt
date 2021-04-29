package feautures.auth

import com.example.feautures.account.domain.CartCookie
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

    override suspend fun syncCart(email: String, cartCookie: CartCookie?): Boolean {
        cartCookie?.let {
            val user = userData[email]!!
            cartCookie.orders.forEach { order ->
                if (!user.cartOrders.contains(order)) user.cartOrders.add(order)
            }
            userData[email] = user
        }
        return true
    }
}