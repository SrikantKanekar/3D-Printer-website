package com.example.database.order

import com.example.features.checkout.domain.Order
import org.litote.kmongo.coroutine.CoroutineCollection

class OrderDataSourceImpl(
    private val orders: CoroutineCollection<Order>
): OrderDataSource {

    override suspend fun insertOrder(order: Order): Boolean {
        TODO("Not yet implemented")
    }
}