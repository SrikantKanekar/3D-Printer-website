package com.example.util.constants

object Auth {
    const val USER_AUTH = "USER_AUTH"
    const val ADMIN_AUTH = "ADMIN_AUTH"

    const val JWT_AUTH_SECRET = "JWT_SECRET"

    const val USERNAME_CLAIM = "username"
    const val EMAIL_CLAIM = "email"
    const val ADMIN_CLAIM = "is_admin"

    const val EMAIL_PASSWORD_INCORRECT = "The email or password is incorrect"
    const val EMAIL_ALREADY_TAKEN = "A user with that email already exists"
    const val PASSWORDS_DO_NOT_MATCH = "passwords do not match"
    const val UNKNOWN_REGISTRATION_ERROR = "An unknown error occurred"
}