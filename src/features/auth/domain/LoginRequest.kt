package com.example.features.auth.domain

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    var email: String,
    var password: String
)
