package com.example.features.history.data

import com.example.features.order.domain.Order

interface HistoryDataSource {

    suspend fun getUserHistoryOrders(email: String): ArrayList<Order>
}