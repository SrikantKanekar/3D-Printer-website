package com.example.features.`object`.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.AdvancedSettings
import com.example.features.`object`.domain.BasicSettings
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

    suspend fun updateFileName(email: String, id: String, fileName: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.fileName = fileName } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateBasicSettings(email: String, id: String, basicSettings: BasicSettings): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.basicSettings = basicSettings } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateAdvancedSettings(email: String, id: String, advancedSettings: AdvancedSettings): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE || it.status == CART }
            .find { it.id == id }
            ?.let { it.advancedSettings = advancedSettings } ?: return false
        return userDataSource.updateUser(user)
    }
}