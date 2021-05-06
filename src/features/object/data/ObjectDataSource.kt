package com.example.features.`object`.data

import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
import com.example.features.`object`.domain.Object

interface ObjectDataSource {

    suspend fun createOrder(fileName: String): Object

    suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean

    suspend fun getOrder(id: String): Object?

    suspend fun updateFileName(id: String, fileName: String): Boolean

    suspend fun updateBasicSetting(id: String, basicSettings: BasicSettings): Boolean

    suspend fun updateAdvancedSetting(id: String, advancedSettings: AdvancedSettings): Boolean
}