package com.example.features.`object`.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum
import kotlinx.serialization.Serializable

@Serializable
data class ObjectQuantityRequest(
    val quantity: Int
) {
    init {
        Validation<ObjectQuantityRequest> {
            ObjectQuantityRequest::quantity {
                minimum(1)
            }
        }.validateAndThrowOnFailure(this)
    }
}