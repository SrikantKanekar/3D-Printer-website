package com.example.features.cart.data

import com.example.database.user.UserDataSource
import com.example.model.Object
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.NONE

class CartRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getCartObjects(email: String): List<Object> {
        val user = userDataSource.getUser(email)
        return user.objects.filter { it.status == CART }
    }

    suspend fun addToCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE }
            .find { it.id == objectId }
            ?.let { it.status = CART } ?: return false
        userDataSource.updateUser(user)
        return true
    }

    suspend fun removeFromCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.status = NONE } ?: return false
        userDataSource.updateUser(user)
        return true
    }
}