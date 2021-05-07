package com.example.features.tracker.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*

class TrackerRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUserTrackingOrders(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == TRACKING })
    }
}