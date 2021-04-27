package com.example.feautures.account.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String,
    val username: String? = null
)
