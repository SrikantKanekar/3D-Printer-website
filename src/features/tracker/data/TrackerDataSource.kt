package com.example.features.tracker.data

import com.example.features.`object`.domain.Object

interface TrackerDataSource {

    suspend fun getUserTrackingOrders(email: String): ArrayList<Object>
}