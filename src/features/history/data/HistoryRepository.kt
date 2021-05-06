package com.example.features.history.data

import com.example.features.order.domain.Order

class HistoryRepository(
    private val historyDataSource: HistoryDataSource
) {
    suspend fun getUserHistoryOrders(email: String): ArrayList<Order> {
        return historyDataSource.getUserHistoryOrders(email)
    }
}