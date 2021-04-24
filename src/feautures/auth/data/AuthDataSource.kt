package com.example.feautures.auth.data

import com.example.feautures.auth.domain.User

interface AuthDataSource {

    suspend fun insert(user: User): Boolean

    suspend fun search(email: String): User?

    suspend fun checkIfEmailExist(email: String): Boolean

    suspend fun getPassword(email: String): String?

    suspend fun getAllUsers(): List<User>
}