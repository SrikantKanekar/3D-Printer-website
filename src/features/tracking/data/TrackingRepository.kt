package com.example.features.tracking.data

import com.example.database.order.OrderDataSource
import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus
import com.example.features.`object`.domain.ObjectStatus.*
import com.example.features.order.domain.Order
import com.example.features.order.domain.OrderStatus.*

class TrackingRepository(
    private val userDataSource: UserDataSource,
    private val orderDataSource: OrderDataSource
) {

    suspend fun getUserTrackingOrders(email: String): List<Order> {
        return orderDataSource
            .getOrdersByUser(email)
            .filter { it.status != DELIVERED }
    }

    suspend fun getUserObjectById(email: String, objectId: String): Object? {
        return userDataSource.getUser(email)
            .objects
            .filter { it.status == TRACKING }
            .find { it.id == objectId }
    }
}