package com.example.features.auth.data

import com.example.database.user.UserDataSource
import com.example.features.objects.domain.ObjectsCookie
import com.example.features.account.domain.User
import com.example.features.auth.domain.checkHashForPassword

class AuthRepository(
    private val userDataSource: UserDataSource
) {
    suspend fun login(email: String, passwordToCheck: String): Boolean {
        val user = userDataSource.getUserOrNull(email) ?: return false
        return checkHashForPassword(passwordToCheck, user.password)
    }

    suspend fun register(user: User): Boolean {
        return userDataSource.insertUser(user)
    }

    suspend fun doesUserExist(email: String): Boolean {
        val user = userDataSource.getUserOrNull(email)
        return user != null
    }

    suspend fun syncCookieObjects(email: String, objectsCookie: ObjectsCookie?): Boolean {
        if (objectsCookie != null) {
            val user = userDataSource.getUser(email)
            objectsCookie.objects.forEach { obj ->
                if (!user.objects.contains(obj)) user.objects.add(obj)
            }
            return userDataSource.updateUser(user)
        }
        return false
    }
}