package com.example.features.account.data

import com.example.features.account.domain.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class AccountDataSourceImpl(
    private val users: CoroutineCollection<User>
): AccountDataSource {

    override suspend fun get(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun update(user: User): Boolean {
        return users.updateOne(User::email eq user.email, user).wasAcknowledged()
    }
}