package com.example.database

import com.example.feautures.auth.domain.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("3d-printer-database")
val users = database.getCollection<User>()