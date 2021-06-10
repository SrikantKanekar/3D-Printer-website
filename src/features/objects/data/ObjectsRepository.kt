package com.example.features.objects.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*

class ObjectsRepository(
    private val userDataSource: UserDataSource
) {

    suspend fun getUserObjects(email: String): List<Object> {
        val user = userDataSource.getUser(email)
        return user.objects.reversed()
    }

    suspend fun deleteUserObject(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        val deleted = user.objects.removeIf { it.id == objectId && it.status == NONE }
        return userDataSource.updateUser(user) and deleted
    }

    suspend fun addToCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE }
            .find { it.id == objectId && it.slicingDetails.uptoDate }
            ?.let { it.status = CART } ?: return false
        return userDataSource.updateUser(user)
    }
}