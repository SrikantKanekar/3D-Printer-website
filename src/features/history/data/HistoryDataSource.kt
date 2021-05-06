package com.example.features.history.data

import com.example.features.`object`.domain.Object

interface HistoryDataSource {

    suspend fun getUserHistoryOrders(email: String): ArrayList<Object>
}