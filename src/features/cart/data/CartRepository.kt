package com.example.features.cart.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.CART
import com.example.features.`object`.domain.ObjectStatus.NONE

class CartRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUserCartObjects(email: String): ArrayList<Object> {
        val user = userDataSource.getUser(email)
        return ArrayList(user.objects.filter { it.status == CART })
    }

    suspend fun removeCartObject(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.status = NONE } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateQuantity(email: String, objectId: String, quantity: Int): Boolean {
        val user = userDataSource.getUser(email)
        if (quantity < 1) return false
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.quantity = quantity } ?: return false
        return userDataSource.updateUser(user)
    }
}