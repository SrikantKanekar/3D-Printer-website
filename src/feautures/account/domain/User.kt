package com.example.feautures.account.domain

import com.example.feautures.checkout.domain.Address
import com.example.feautures.notification.domain.Notification
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val username: String,
    val address: Address = Address(),
    val wishlist: ArrayList<String> = ArrayList(),
    val cartOrders: ArrayList<String> = ArrayList(),
    val currentOrders: ArrayList<String> = ArrayList(),
    val orderHistory: ArrayList<String> = ArrayList(),
    val notification: ArrayList<Notification> = ArrayList(),
)