package com.example.data.models

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class UserIdPrincipal(
    val name: String
) : Principal