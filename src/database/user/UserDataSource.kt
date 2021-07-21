package com.example.database.user

import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import com.example.model.User

interface UserDataSource {

    suspend fun insertUser(user: User)

    suspend fun getUserOrNull(email: String): User?

    suspend fun getUser(email: String): User

    suspend fun updateUser(user: User)

    suspend fun createObject(body: ObjectCreateRequest): Object
}