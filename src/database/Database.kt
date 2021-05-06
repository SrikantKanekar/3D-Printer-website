package com.example.database

import com.example.features.account.domain.User
import com.example.features.order.domain.Object
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

const val DATABASE_NAME = "3D-PRINTER-DATABASE"

// User
const val COLLECTION_USER = "USER"

// Orders
const val COLLECTION_WISHLIST = "WISHLIST"
const val COLLECTION_CART = "CART"
const val COLLECTION_PROCESSING = "PROCESSING"
const val COLLECTION_HISTORY = "HISTORY"

val mongoDbString = System.getenv("MONGODB_URI") ?: "mongodb://localhost"
private val client = KMongo.createClient(mongoDbString).coroutine
private val database = client.getDatabase(DATABASE_NAME)

// User
val users = database.getCollection<User>(COLLECTION_USER)

// Orders
val wishlistOrders = database.getCollection<Object>(COLLECTION_WISHLIST)
val cartOrders = database.getCollection<Object>(COLLECTION_CART)
val processingOrders = database.getCollection<Object>(COLLECTION_PROCESSING)
val historyOrders = database.getCollection<Object>(COLLECTION_HISTORY)