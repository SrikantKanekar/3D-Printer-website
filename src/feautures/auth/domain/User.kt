package com.example.feautures.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String
)
