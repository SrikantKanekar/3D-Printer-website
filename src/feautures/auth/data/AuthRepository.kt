package com.example.feautures.auth.data

import com.example.feautures.auth.domain.User
import com.example.util.checkHashForPassword

class AuthRepository(
    private val authDataSource: AuthDataSource
) {

    suspend fun register(user: User): Boolean {
        return authDataSource.insert(user)
    }

    suspend fun checkIfUserExists(email: String): Boolean {
        return authDataSource.checkIfEmailExist(email)
    }

    suspend fun login(email: String, passwordToCheck: String): Boolean {
        val hashedPassword = authDataSource.getPassword(email) ?: return false
        return checkHashForPassword(passwordToCheck, hashedPassword)
    }

    suspend fun getAllUsers(): List<User> {
        return authDataSource.getAllUsers()
    }
}