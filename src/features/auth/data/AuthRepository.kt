package com.example.features.auth.data

import com.example.database.user.UserDataSource
import com.example.features.wishlist.domain.WishlistCookie
import com.example.features.account.domain.User
import com.example.features.auth.domain.checkHashForPassword

class AuthRepository(
    private val userDataSource: UserDataSource
) {

    suspend fun register(user: User): Boolean {
        return userDataSource.insertUser(user)
    }

    suspend fun doesUserExist(email: String): Boolean {
        return userDataSource.doesUserExist(email)
    }

    suspend fun login(email: String, passwordToCheck: String): Boolean {
        val hashedPassword = userDataSource.getUserHashedPassword(email) ?: return false
        return checkHashForPassword(passwordToCheck, hashedPassword)
    }

    suspend fun syncOrders(email: String, wishlistCookie: WishlistCookie?): Boolean {
        wishlistCookie?.let {
            val user = userDataSource.getUser(email)!!
            wishlistCookie.orders.forEach { order ->
                if (!user.wishlist.contains(order)) user.wishlist.add(order)
            }
            return userDataSource.updateUser(user)
        } ?: return true
    }
}