package com.example.features.account.domain.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@Serializable
data class UpdateAccountRequest(
    val username: String
) {
    init {
        Validation<UpdateAccountRequest> {
            UpdateAccountRequest::username{
                minLength(2)
                maxLength(50)
            }
        }.validateAndThrowOnFailure(this)
    }
}

