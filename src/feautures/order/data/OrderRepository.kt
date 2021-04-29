package com.example.feautures.order.data

import com.example.feautures.order.domain.AdvancedSettings
import com.example.feautures.order.domain.BasicSettings
import com.example.feautures.order.domain.Order

class OrderRepository(
    private val orderDataSource: OrderDataSource
) {

    suspend fun createOrder(fileName: String): Order? {
        val order = Order(fileName = fileName)
        val wasAcknowledged = orderDataSource.createOrder(order)
        return if (wasAcknowledged) order else null
    }

    suspend fun addOrderToUserCart(email: String, orderId: String): Boolean {
        return orderDataSource.addOrderToUserCart(email, orderId)
    }

    suspend fun getOrder(id: String): Order?{
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