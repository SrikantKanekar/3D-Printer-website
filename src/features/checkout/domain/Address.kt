package com.example.features.checkout.domain

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val city: String = "",
    val state: String = "",
    val country: String = ""
)
