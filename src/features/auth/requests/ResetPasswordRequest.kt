package com.example.features.auth.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val oldPassword: String,
    val newPassword: String,
    val confirmPassword: String
) {
    init {
        Validation<ResetPasswordRequest> {
            ResetPasswordRequest::newPassword{
                minLength(4)
            }
            ResetPasswordRequest::confirmPassword{
                minLength(4)
            }
        }.validateAndThrowOnFailure(this)
    }
}
