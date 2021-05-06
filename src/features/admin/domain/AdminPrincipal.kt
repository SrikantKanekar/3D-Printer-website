package com.example.features.admin.domain

import io.ktor.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class AdminPrincipal(
    val name: String
): Principal
