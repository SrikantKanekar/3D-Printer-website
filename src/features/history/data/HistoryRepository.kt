package com.example.features.history.data

import com.example.features.order.domain.Object

class HistoryRepository(
    private val historyDataSource: HistoryDataSource
) {
    suspend fun getUserHistoryOrders(email: String): ArrayList<Object> {
        return historyDataSource.getUserHistoryOrders(email)
    }
}