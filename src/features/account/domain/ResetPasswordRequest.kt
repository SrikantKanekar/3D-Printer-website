package com.example.features.account.domain

data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
)
