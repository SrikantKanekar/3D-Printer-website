package com.example.features.account.data

import com.example.features.account.domain.User

interface AccountDataSource {

    suspend fun get(email: String): User?

    suspend fun update(user: User): Boolean
}