package com.example.database.user

import com.example.features.`object`.domain.Object
import com.example.features.account.domain.User
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class UserDataSourceImpl(
    private val users: CoroutineCollection<User>
) : UserDataSource {

    override suspend fun insertUser(user: User): Boolean {
        return users.insertOne(user).wasAcknowledged()
    }

    override suspend fun getUserOrNull(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun getUser(email: String): User {
        return users.findOne(User::email eq email)!!
    }

    override suspend fun updateUser(user: User): Boolean {
        return users.updateOne(User::email eq user.email, user).wasAcknowledged()
    }

    override suspend fun createNewObject(fileName: String): Object {
        return Object(filename = fileName)
    }
}