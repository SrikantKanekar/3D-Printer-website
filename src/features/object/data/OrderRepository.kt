package com.example.features.`object`.data

import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
import com.example.features.`object`.domain.Object

class OrderRepository(
    private val objectDataSource: ObjectDataSource
) {

    suspend fun createOrder(fileName: String): Object {
        return objectDataSource.createOrder(fileName)
    }

    suspend fun addOrderToUserWishlist(email: String, orderId: String): Boolean {
        return objectDataSource.addOrderToUserWishlist(email, orderId)
    }

    suspend fun getOrder(id: String): Object? {
        return objectDataSource.getOrder(id)
    }

    suspend fun updateFileName(id: String, fileName: String): Boolean {
        return objectDataSource.updateFileName(id, fileName)
    }

    suspend fun updateBasicSettings(id: String, basicSettings: BasicSettings): Boolean {
        return objectDataSource.updateBasicSetting(id, basicSettings)
    }

    suspend fun updateAdvancedSettings(id: String, advancedSettings: AdvancedSettings): Boolean {
        return objectDataSource.updateAdvancedSetting(id, advancedSettings)
    }
}