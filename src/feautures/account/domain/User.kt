package com.example.feautures.account.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val username: String,
    val address: Address = Address(),
    val cartOrders: ArrayList<String> = ArrayList(),
    val currentOrders: ArrayList<String> = ArrayList(),
    val orderHistory: ArrayList<String> = ArrayList(),
    val notification: ArrayList<String> = ArrayList(),
)

@Serializable
data class Address(
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)

@Serializable
data class Notification(
    val message: String
)