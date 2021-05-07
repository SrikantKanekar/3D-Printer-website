package com.example.features.history.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.COMPLETED

class HistoryRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun getUserHistoryOrders(email: String): ArrayList<Object> {
        return ArrayList(userDataSource.getUser(email).objects.filter { it.status == COMPLETED })
    }
}