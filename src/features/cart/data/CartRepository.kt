package com.example.features.cart.data

import com.example.features.order.domain.Order

class CartRepository(
    private val cartDataSource: CartDataSource
) {

    suspend fun getUserCartOrders(email: String): ArrayList<Order> {
        return cartDataSource.getUserCartOrders(email)
    }

    suspend fun removeCartOrder(email: String, orderId: String): Boolean {
        return cartDataSource.removeCartOrder(email, orderId)
    }
}