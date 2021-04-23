package com.example.data.database

import com.example.data.database.Constants.TEST_USER_NAME
import com.example.data.database.Constants.TEST_USER_PASSWORD
import com.example.data.models.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("3d-printer-database")
private val users = database.getCollection<User>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {

    // For testing
    if (email == TEST_USER_NAME && passwordToCheck == TEST_USER_PASSWORD) {
        return true
    }

    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return checkHashForPassword(passwordToCheck, actualPassword)
}