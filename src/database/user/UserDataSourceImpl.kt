package com.example.database.user

import com.example.features.`object`.requests.ObjectCreateRequest
import com.example.model.Object
import com.example.model.User
import com.example.util.DatabaseException
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq

class UserDataSourceImpl(
    private val users: CoroutineCollection<User>
) : UserDataSource {

    override suspend fun insertUser(user: User) {
        val inserted = users.insertOne(user).wasAcknowledged()
        if (!inserted) throw DatabaseException(
            "error inserting user with email ${user.email}"
        )
    }

    override suspend fun getUserOrNull(email: String): User? {
        return users.findOne(User::email eq email)
    }

    override suspend fun getUser(email: String): User {
        return users.findOne(User::email eq email)!!
    }

    override suspend fun getAll(): List<User> {
        return users.find().toList()
    }

    override suspend fun updateUser(user: User) {
        val updated = users
            .updateOne(User::email eq user.email, user)
            .wasAcknowledged()
        if (!updated) throw DatabaseException(
            "error updating user of email ${user.email}"
        )
    }

    override suspend fun createObject(body: ObjectCreateRequest, email: String): Object {
        return Object(
            id = body.id,
            name = body.name,
            fileUrl = body.fileUrl,
            fileExtension = body.fileExtension,
            imageUrl = body.imageUrl,
            userEmail = email
        )
    }
}