package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    var password: String,
    var username: String,
    val isAdmin: Boolean = false,
    var address: Address = Address(),
    val objects: ArrayList<Object> = ArrayList(),
    val orderIds: ArrayList<String> = ArrayList(),
    val notification: ArrayList<Notification> = ArrayList(),
)