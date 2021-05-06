package com.example.features.tracker.data

import com.example.features.order.domain.Order

class TrackerRepository(
    private val trackerDataSource: TrackerDataSource
) {

    suspend fun getUserTrackingOrders(email: String): ArrayList<Order> {
        return trackerDataSource.getUserTrackingOrders(email)
    }
}