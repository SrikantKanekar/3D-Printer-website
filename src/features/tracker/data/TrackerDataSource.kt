package com.example.features.tracker.data

import com.example.features.order.domain.Order

interface TrackerDataSource {

    suspend fun getUserTrackingOrders(email: String): ArrayList<Order>
}