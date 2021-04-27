package com.example.feautures.account.data

import com.example.feautures.account.domain.User

class AccountRepository(
    private val accountDataSource: AccountDataSource
) {
    suspend fun getUser(email: String): User? {
        return accountDataSource.get(email)
    }

    suspend fun updateUser(user: User): Boolean {
        return accountDataSource.update(user)
    }
}