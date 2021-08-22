package com.example.database

import com.example.model.Order
import com.example.model.SpecialRequest
import com.example.model.User
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

const val DATABASE_NAME = "3D-PRINTER-DATABASE"
const val COLLECTION_USER = "USERS"
const val COLLECTION_ORDER = "ORDERS"
const val COLLECTION_DIRECT_REQUEST = "DIRECT_REQUEST"
const val COLLECTION_SPECIAL_REQUEST = "SPECIAL_REQUEST"

val mongoDbString = System.getenv("MONGODB_URI") ?: "mongodb://localhost"
private val client = KMongo.createClient(mongoDbString).coroutine
private val database = client.getDatabase(DATABASE_NAME)

val users = database.getCollection<User>(COLLECTION_USER)
val orders = database.getCollection<Order>(COLLECTION_ORDER)
val directRequests = database.getCollection<SpecialRequest>(COLLECTION_DIRECT_REQUEST)
val specialRequests = database.getCollection<SpecialRequest>(COLLECTION_SPECIAL_REQUEST)