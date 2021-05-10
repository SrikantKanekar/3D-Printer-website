package com.example.features.checkout.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.checkout.domain.Address
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*

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
        val order = orderDataSource.creteNewOrder(userEmail = email)
        user.objects
            .filter { it.status == CART }
            .forEach { obj ->
                order.objectIds.add(obj.id)
                obj.status = TRACKING
            }
        var updated = false
        if (order.objectIds.size > 0){
            val ordered = orderDataSource.insertOrder(order)
            if (ordered) {
                user.orderIds.add(order.id)
                updated = userDataSource.updateUser(user)
            }
        }
        return updated
    }
}