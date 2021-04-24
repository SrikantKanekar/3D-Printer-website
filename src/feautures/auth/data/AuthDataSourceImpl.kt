package com.example.feautures.auth.data

import com.example.feautures.auth.domain.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AuthDataSourceImpl(
    private val users: CoroutineCollection<User>
): AuthDataSource {

    override suspend fun insert(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun search(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun checkIfEmailExist(email: String): Boolean {
        return users.findOne(User::email eq email) != null
    }

    override suspend fun getPassword(email: String): String? {
        return users.findOne(User::email eq email)?.password
    }

    override suspend fun getAllUsers(): List<User> {
        return users.find().toList()
    }
}