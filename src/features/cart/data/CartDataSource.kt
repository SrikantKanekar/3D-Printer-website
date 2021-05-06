package com.example.features.cart.data

import com.example.features.`object`.domain.Object

interface CartDataSource {

    suspend fun getUserCartOrders(email: String): ArrayList<Object>

    suspend fun removeCartOrder(email: String, orderId: String): Boolean

    suspend fun getAllCartOrders(): ArrayList<Object>
}