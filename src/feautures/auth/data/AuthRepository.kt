package com.example.feautures.auth.data

import com.example.feautures.wishlist.domain.WishlistCookie
import com.example.feautures.account.domain.User
import com.example.feautures.auth.domain.checkHashForPassword

class AuthRepository(
    private val authDataSource: AuthDataSource
) {

    suspend fun register(user: User): Boolean {
        return authDataSource.insert(user)
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return authDataSource.checkIfEmailExist(email)
    }

    suspend fun login(email: String, passwordToCheck: String): Boolean {
        val hashedPassword = authDataSource.getPassword(email) ?: return false
        return checkHashForPassword(passwordToCheck, hashedPassword)
    }

    suspend fun syncOrders(email: String, wishlistCookie: WishlistCookie?): Boolean {
        return authDataSource.syncOrders(email, wishlistCookie)
    }
}