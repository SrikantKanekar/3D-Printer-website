package com.example.features.auth.domain

import io.ktor.locations.*

@Location("/login/{type?}") class Login(val type: String = "")