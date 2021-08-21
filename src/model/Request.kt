package com.example.model

data class Request(
    val _id: String,
    val fulfilled: Boolean = false,
    val userEmail: String,
    val fileUrl: String,
    val setting: Setting,
    val slicingDetails: SlicingDetails = SlicingDetails(),
    val requestedAt: String,
    val fulfilledAt: String? = null
)
