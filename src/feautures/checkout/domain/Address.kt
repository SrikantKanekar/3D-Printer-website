package com.example.feautures.checkout.domain

import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val city: String? = null,
    val state: String? = null,
    val country: String? = null
)
