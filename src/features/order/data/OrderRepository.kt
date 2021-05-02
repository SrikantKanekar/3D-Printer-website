package com.example.features.order.data

import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Order

class OrderRepository(
    private val orderDataSource: OrderDataSource
) {

    suspend fun createOrder(fileName: String): Order {
        return orderDataSource.createOrder(fileName)
    }

    suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        return orderDataSource.addOrderToUserWishlist(email, orderId)
    }

    suspend fun getOrder(id: String): Order? {
        return orderDataSource.getOrder(id)
    }

    suspend fun updateFileName(id: String, fileName: String): Boolean {
        return orderDataSource.updateFileName(id, fileName)
    }

    suspend fun updateBasicSettings(id: String, basicSettings: BasicSettings): Boolean {
        return orderDataSource.updateBasicSetting(id, basicSettings)
    }

    suspend fun updateAdvancedSettings(id: String, advancedSettings: AdvancedSettings): Boolean {
        return orderDataSource.updateAdvancedSetting(id, advancedSettings)
    }
}