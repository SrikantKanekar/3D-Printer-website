package com.example.features.checkout.domain

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val firstname: String = "",
    val lastname: String = "",
    val phoneNumber: Long? = null,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val pinCode: Int? = null
)
