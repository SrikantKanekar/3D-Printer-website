package com.example.database.user

import com.example.model.Object
import com.example.model.User
import com.example.util.DatabaseException
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class UserDataSourceImpl(
    private val users: CoroutineCollection<User>
) : UserDataSource {

    override suspend fun insertUser(user: User): Boolean {
        val inserted = users.insertOne(user).wasAcknowledged()
        if (!inserted) throw DatabaseException("error inserting user")
        return true
    }

    override suspend fun getUserOrNull(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun getUser(email: String): User {
        return users.findOne(User::email eq email)!!
    }

    override suspend fun updateUser(user: User): Boolean {
        val updated = users.updateOne(User::email eq user.email, user).wasAcknowledged()
        if (!updated) throw DatabaseException("error updating user")
        return true
    }

    override suspend fun createObject(
        id: String,
        name: String,
        fileUrl: String,
        imageUrl: String,
        fileExtension: String
    ): Object {
        return Object(id = id, name = name, fileUrl = fileUrl, fileExtension = fileExtension, imageUrl = imageUrl)
    }
}