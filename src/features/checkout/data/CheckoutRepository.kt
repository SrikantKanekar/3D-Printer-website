package com.example.features.checkout.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.checkout.domain.Address
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.checkout.domain.Order

class CheckoutRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource
) {
    suspend fun getUserCartObjects(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == CART })
    }

    suspend fun getUserAddress(email: String): Address {
        return userDataSource.getUser(email).address
    }

    suspend fun removeObjectFromCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.status = NONE } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateUserAddress(email: String, address: Address): Boolean {
        val user = userDataSource.getUser(email)
        user.address = address
        return userDataSource.updateUser(user)
    }

    suspend fun checkoutSuccess(email: String): Boolean {
        val user = userDataSource.getUser(email)
        val order = Order(
            userEmail = email,
            objects = ArrayList()
        )
        user.objects
            .filter { it.status == CART }
            .forEach {
                order.objects.add(it)
                it.status = TRACKING
            }
        var updated = false
        if (order.objects.size > 0){
            val ordered = orderDataSource.insertOrder(order)
            if (ordered) updated = userDataSource.updateUser(user)
        }
        return updated
    }
}