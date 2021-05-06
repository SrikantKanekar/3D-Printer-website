package com.example.database.user

import com.example.features.account.domain.User

interface UserDataSource {

    suspend fun insertUser(user: User): Boolean

    suspend fun getUser(email: String): User?

    suspend fun updateUser(user: User): Boolean

    suspend fun doesUserExist(email: String): Boolean

    suspend fun getUserHashedPassword(email: String): String?
}