package com.example.features.`object`.data

import com.example.database.user.UserDataSource
import com.example.model.Object
import com.example.model.Setting
import com.example.model.SlicingDetails
import com.example.util.enums.ObjectStatus.CART
import com.example.util.enums.ObjectStatus.NONE
import kotlin.random.Random

class ObjectRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun createObject(id: String, name: String, fileUrl: String, imageUrl: String, fileExtension: String): Object {
        return userDataSource.createObject(id, name, fileUrl, imageUrl, fileExtension)
    }

    suspend fun getUserObjects(email: String): List<Object> {
        val user = userDataSource.getUser(email)
        return user.objects.reversed()
    }

    suspend fun getUserObject(email: String, id: String): Object? {
        val user = userDataSource.getUser(email)
        return user.objects.find { it.id == id }
    }

    suspend fun addUserObject(email: String, obj: Object): Boolean {
        val user = userDataSource.getUser(email)
        user.objects.add(obj)
        return userDataSource.updateUser(user)
    }

    /**
     * use object ID value to get the object file, slice the file in octoPrint
     */
    suspend fun slice(fileUrl: String): SlicingDetails? {
        val time = Random.nextLong(2000000, 40000000)
        val materialWeight = Random.nextInt(10, 100)
        val materialCost = Random.nextInt(500, 1500)
        val electricityCost = Random.nextInt(100, 300)
        val totalPrice = Random.nextInt(2000, 4000)
        return SlicingDetails(
            time = time,
            materialWeight = materialWeight,
            materialCost = materialCost,
            electricityCost = electricityCost,
            totalPrice = totalPrice
        )
    }

    suspend fun sliceUserObject(email: String, objectId: String): SlicingDetails? {
        val user = userDataSource.getUser(email)
        val obj = user.objects
            .filter { it.status == NONE && !it.slicingDetails.uptoDate }
            .find { it.id == objectId }

        if (obj != null){
            val result = slice(obj.fileUrl)
            result?.let {
                user.objects
                    .find { it.id == objectId }
                    ?.apply {
                        slicingDetails.uptoDate = true
                        slicingDetails.time = result.time
                        slicingDetails.materialWeight = result.materialWeight
                        slicingDetails.materialCost = result.materialCost
                        slicingDetails.electricityCost = result.electricityCost
                        slicingDetails.totalPrice = result.totalPrice
                    }
                userDataSource.updateUser(user)
            }
            return result
        }
        return null
    }

    suspend fun addToCart(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE }
            .find { it.id == objectId && it.slicingDetails.uptoDate }
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

    suspend fun deleteUserObject(email: String, objectId: String): Boolean {
        val user = userDataSource.getUser(email)
        val deleted = user.objects.removeIf { it.id == objectId && it.status == NONE }
        if (deleted) return userDataSource.updateUser(user)
        return false
    }


    suspend fun updateFilename(email: String, id: String, fileName: String): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .find { it.id == id }
            ?.let { it.name = fileName } ?: return false
        return userDataSource.updateUser(user)
    }

    suspend fun updateSetting(email: String, id: String, setting: Setting): Boolean {
        val user = userDataSource.getUser(email)
        user.objects
            .filter { it.status == NONE }
            .find { it.id == id }
            ?.let {
                it.setting = setting
                it.slicingDetails.uptoDate = false
            } ?: return false
        return userDataSource.updateUser(user)
    }
}