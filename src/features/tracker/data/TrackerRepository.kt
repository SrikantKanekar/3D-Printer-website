package com.example.features.tracker.data

import com.example.features.order.domain.Object

class TrackerRepository(
    private val trackerDataSource: TrackerDataSource
) {

    suspend fun getUserTrackingOrders(email: String): ArrayList<Object> {
        return trackerDataSource.getUserTrackingOrders(email)
    }
}