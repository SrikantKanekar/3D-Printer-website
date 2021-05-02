package com.example.features.checkout.data

import com.example.features.checkout.domain.Address
import com.example.features.order.domain.Order

class CheckoutRepository(
    private val checkoutDataSource: CheckoutDataSource
) {
    suspend fun getUserCartOrders(email: String): ArrayList<Order>{
        return checkoutDataSource.getUserCartOrders(email)
    }

    suspend fun removeCartOrder(email: String, orderId: String): Boolean{
        return checkoutDataSource.removeCartOrder(email, orderId)
    }

    suspend fun updateUserAddress(email: String, address: Address): Boolean{
        return checkoutDataSource.updateUserAddress(email, address)
    }
}