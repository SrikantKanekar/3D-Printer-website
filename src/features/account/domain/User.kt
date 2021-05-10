package com.example.features.account.domain

import com.example.features.`object`.domain.Object
import com.example.features.checkout.domain.Address
import com.example.features.notification.domain.Notification
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val username: String,
    var address: Address = Address(),
    val objects: ArrayList<Object> = ArrayList(),
    val orderIds: ArrayList<String> = ArrayList(),
    val notification: ArrayList<Notification> = ArrayList(),
)