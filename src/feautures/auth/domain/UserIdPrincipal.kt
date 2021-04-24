package com.example.feautures.auth.domain

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserIdPrincipal(
    val email: String
) : Principal