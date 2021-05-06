package com.example.features.cart.data

import com.example.features.order.domain.Object

class CartRepository(
    private val cartDataSource: CartDataSource
) {
    suspend fun getUserCartOrders(email: String): ArrayList<Object> {
        return cartDataSource.getUserCartOrders(email)
    }

    suspend fun removeCartOrder(email: String, orderId: String): Boolean {
        return cartDataSource.removeCartOrder(email, orderId)
    }

    // For Testing
    suspend fun getAllCartOrders(): ArrayList<Object> {
        return cartDataSource.getAllCartOrders()
    }
}