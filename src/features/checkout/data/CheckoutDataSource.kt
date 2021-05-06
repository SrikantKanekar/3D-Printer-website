package com.example.features.checkout.data

import com.example.features.checkout.domain.Address
import com.example.features.order.domain.Order

interface CheckoutDataSource {

    suspend fun getUserCartOrders(email: String): ArrayList<Order>

    suspend fun getUserAddress(email: String): Address

    suspend fun removeCartOrder(email: String, orderId: String): Boolean

    suspend fun updateUserAddress(email: String, address: Address): Boolean

    suspend fun checkoutSuccess(email: String): Boolean
}