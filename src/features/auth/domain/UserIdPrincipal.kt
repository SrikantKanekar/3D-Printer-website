package com.example.features.auth.domain

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserIdPrincipal(
    val email: String
) : Principal