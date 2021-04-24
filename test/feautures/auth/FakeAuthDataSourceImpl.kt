package com.example.feautures.auth

import com.example.feautures.auth.data.AuthDataSource
import com.example.feautures.auth.domain.User

class FakeAuthDataSourceImpl(
    private val userData: HashMap<String, User>,
): AuthDataSource {

    override suspend fun insert(user: User): Boolean {
        userData[user.email] = user
        return true
    }

    override suspend fun search(email: String): User? {
        return userData[email]
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return userData.containsKey(email)
    }

    override suspend fun getPassword(email: String): String? {
        return userData[email]?.password
    }

    override suspend fun getAllUsers(): List<User> {
        return userData.values.toList()
    }
}