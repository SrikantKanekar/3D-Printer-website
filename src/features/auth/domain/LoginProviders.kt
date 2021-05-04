package com.example.features.auth.domain

import io.ktor.auth.*
import io.ktor.http.*

val loginProviders = listOf(
    OAuthServerSettings.OAuth2ServerSettings(
        name = "google",
        authorizeUrl = "https://accounts.google.com/o/oauth2/auth",
        accessTokenUrl = "https://www.googleapis.com/oauth2/v3/token",
        requestMethod = HttpMethod.Post,
        clientId = "1097772976324-vh6gsiqs428fhd82p9fa3jp6a86gstk5.apps.googleusercontent.com",
        clientSecret = "0QX93ZHxaboyy4voEhLEVS-w",
        defaultScopes = listOf("https://www.googleapis.com/auth/plus.login")
    )
).associateBy { it.name }