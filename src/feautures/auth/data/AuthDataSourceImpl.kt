package com.example.feautures.auth.data

import com.example.feautures.account.domain.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AuthDataSourceImpl(
    private val users: CoroutineCollection<User>
): AuthDataSource {

    override suspend fun insert(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return users.findOne(User::email eq email) != null
    }

    override suspend fun getPassword(email: String): String? {
        return users.findOne(User::email eq email)?.password
    }
}