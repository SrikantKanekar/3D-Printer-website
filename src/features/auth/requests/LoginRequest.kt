package com.example.features.auth.domain

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    var email: String,
    var password: String
) {
    init {
        Validation<LoginRequest> {
            LoginRequest::email required {}
            LoginRequest::password {
                minLength(4)
            }
        }.validateAndThrowOnFailure(this)
    }
}

