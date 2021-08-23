package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class DirectRequest(
    val _id: String,
    var name: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String,
    val userEmail: String,
    var fulfilled: Boolean = false,
    var slicing: Slicing = Slicing(),
    val requestedAt: String,
    var fulfilledAt: String? = null,
)
