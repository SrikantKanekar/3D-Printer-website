package com.example.feautures.auth.data

import com.example.feautures.wishlist.domain.WishlistCookie
import com.example.feautures.account.domain.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AuthDataSourceImpl(
    private val users: CoroutineCollection<User>
): AuthDataSource {

    override suspend fun insert(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return users.findOne(User::email eq email) != null
    }

    override suspend fun getPassword(email: String): String? {
        return users.findOne(User::email eq email)?.password
    }

    override suspend fun syncOrders(email: String, wishlistCookie: WishlistCookie?): Boolean {
        wishlistCookie?.let {
            val user = users.findOne(User::email eq email)!!
            wishlistCookie.orders.forEach { order ->
                if (!user.wishlist.contains(order)) user.wishlist.add(order)
            }
            return users.updateOne(User::email eq user.email, user).wasAcknowledged()
        } ?: return true
    }
}