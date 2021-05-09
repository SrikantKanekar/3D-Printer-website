package com.example.features.auth.domain

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserPrincipal(
    val email: String
) : Principal