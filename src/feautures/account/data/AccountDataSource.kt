package com.example.feautures.account.data

import com.example.feautures.account.domain.User

interface AccountDataSource {

    suspend fun get(email: String): User?

    suspend fun update(user: User): Boolean
}