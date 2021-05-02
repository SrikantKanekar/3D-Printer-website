package com.example.features.order.data

import com.example.features.order.domain.AdvancedSettings
import com.example.features.order.domain.BasicSettings
import com.example.features.order.domain.Order

interface OrderDataSource {

    suspend fun createOrder(fileName: String): Order

    suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean

    suspend fun getOrder(id: String): Order?

    suspend fun updateFileName(id: String, fileName: String): Boolean

    suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean

    suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean
}