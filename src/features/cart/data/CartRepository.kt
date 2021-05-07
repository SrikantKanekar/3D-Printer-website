package com.example.features.cart.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.CART
import com.example.features.`object`.domain.ObjectStatus.NONE

class CartRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUserCartOrders(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == CART })
    }

    suspend fun removeCartOrder(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects.find { it.id == objectId }?.status = NONE
        return userDataSource.updateUser(user)
    }
}