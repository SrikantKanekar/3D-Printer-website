package com.example.database

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private const val DATABASE_NAME = "3D-PRINTER-DATABASE"

// User
const val COLLECTION_USER = "USER"

// Orders
const val COLLECTION_WISHLIST = "WISHLIST"
const val COLLECTION_CART = "CART"
const val COLLECTION_PROCESSING = "PROCESSING"
const val COLLECTION_HISTORY = "HISTORY"

private const val connectionString = "mongodb+srv://admin:oziPlEVFkEeLuajk@3design.n5d76.mongodb.net/3Design-database?retryWrites=true&w=majority"
private val client = KMongo.createClient(connectionString).coroutine
private val database = client.getDatabase(DATABASE_NAME)

// User
val users = database.getCollection<User>(COLLECTION_USER)

// Orders
val wishlistOrders = database.getCollection<Order>(COLLECTION_WISHLIST)
val cartOrders = database.getCollection<Order>(COLLECTION_CART)
val processingOrders = database.getCollection<Order>(COLLECTION_PROCESSING)
val historyOrders = database.getCollection<Order>(COLLECTION_HISTORY)