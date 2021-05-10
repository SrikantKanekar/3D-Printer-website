package com.example.database.user

import com.example.features.`object`.domain.Object
import com.example.features.account.domain.User

interface UserDataSource {

    suspend fun insertUser(user: User): Boolean

    suspend fun getUserOrNull(email: String): User?

    suspend fun getUser(email: String): User

    suspend fun updateUser(user: User): Boolean

    suspend fun createNewObject(fileName: String): Object


}