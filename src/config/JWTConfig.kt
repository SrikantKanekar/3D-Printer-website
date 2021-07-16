package com.example.config

data class JWTConfig(
    var secret: String,
    var issuer: String,
    var audience: String,
    var realm: String,
)
