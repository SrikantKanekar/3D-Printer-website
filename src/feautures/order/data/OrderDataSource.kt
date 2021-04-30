package com.example.feautures.order.data

import com.example.feautures.order.domain.AdvancedSettings
import com.example.feautures.order.domain.BasicSettings
import com.example.feautures.order.domain.Order

interface OrderDataSource {

    suspend fun createOrder(order: Order): Boolean

    suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean

    suspend fun getOrder(id: String): Order?

    suspend fun updateFileName(id: String, fileName: String): Boolean

    suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean

    suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean
}