package com.example.database.user

import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import com.example.model.User

interface UserDataSource {

    suspend fun insertUser(user: User): Boolean

    suspend fun getUserOrNull(email: String): User?

    suspend fun getUser(email: String): User

    suspend fun updateUser(user: User): Boolean

    suspend fun createObject(body: ObjectCreateRequest): Object
}