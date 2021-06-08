package com.example.features.`object`.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.AdvancedSetting
import com.example.features.`object`.domain.BasicSetting
import com.example.features.`object`.domain.IntermediateSetting
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*

class ObjectRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun createNewObject(fileName: String): Object {
        return userDataSource.createNewObject(fileName)
    }

    suspend fun addUserObject(email: String, obj: Object): Boolean {
        val user = userDataSource.getUser(email)
        user.objects.add(obj)
        return userDataSource.updateUser(user)
    }

    suspend fun getUserObject(email: String, id: String): Object? {
        val user = userDataSource.getUser(email)
        return user.objects.find { it.id == id }
    }

    suspend fun addToCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE }
            .find { it.id == objectId }
            ?.let { it.status = CART } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun removeFromCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == CART }
            .find { it.id == objectId }
            ?.let { it.status = NONE } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateQuantity(email: String, objectId: String, quantity: Int): Boolean {
        val user = userDataSource.getUser(email)
        if (quantity < 1) return false
        user.objects
            .filter { it.status == NONE }
            .find { it.id == objectId }
            ?.let { it.quantity = quantity } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateFilename(email: String, id: String, fileName: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.filename = fileName } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateBasicSettings(email: String, id: String, basicSetting: BasicSetting): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.basicSetting = basicSetting } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateIntermediateSettings(email: String, id: String, intermediateSetting: IntermediateSetting): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.intermediateSetting = intermediateSetting } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateAdvancedSettings(email: String, id: String, advancedSetting: AdvancedSetting): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.advancedSetting = advancedSetting } ?: return false
        return userDataSource.updateUser(user)
    }
}