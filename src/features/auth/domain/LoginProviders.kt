package com.example.features.auth.domain

import io.ktor.auth.*
import io.ktor.http.*

val loginProviders = listOf(
    OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
        accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
        requestMethod = HttpMethod.Post,
        clientId = "***.apps.googleusercontent.com",
        clientSecret = "***",
        defaultScopes = listOf("https://www.googleapis.com/auth/plus.login")
    )
).associateBy { it.name }