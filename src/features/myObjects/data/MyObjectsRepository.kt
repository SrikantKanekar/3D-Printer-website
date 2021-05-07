package com.example.features.myObjects.data

import com.example.database.user.UserDataSource
import com.example.features.`object`.domain.Object
import com.example.features.`object`.domain.ObjectStatus.*
import java.io.File

class MyObjectsRepository(
    private val userDataSource: UserDataSource
) {

    suspend fun getMyObjects(email: String): ArrayList<Object> {
        val user = userDataSource.getUser(email)
        return ArrayList(user.objects.filter { it.status == NONE })
    }

    fun deleteServerObjectFile(objectId: String): Boolean {
        return File("uploads/$objectId").delete()
    }

    suspend fun deleteUserObject(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        val deleted = user.objects.removeIf { it.id == objectId && it.status == NONE }
        return userDataSource.updateUser(user) and deleted
    }

    suspend fun addToCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects.find { it.id == objectId }?.status = CART
        return userDataSource.updateUser(user)
    }
}