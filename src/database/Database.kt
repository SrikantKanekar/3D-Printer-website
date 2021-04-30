package com.example.database

import com.example.features.account.domain.User
import com.example.features.order.domain.Order
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

private const val connectionString = "mongodb+srv://admin:oziPlEVFkEeLuajk@3design.n5d76.mongodb.net/3Design-database?retryWrites=true&w=majority"
private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("3Design-database")
val users = database.getCollection<User>()
val currentOrders = database.getCollection<Order>("current-orders")
val completedOrders = database.getCollection<Order>("completed-orders")