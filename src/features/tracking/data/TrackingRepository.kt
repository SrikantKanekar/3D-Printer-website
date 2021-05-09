package com.example.features.tracking.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*

class TrackingRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUserTrackingobjects(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == TRACKING })
    }
}