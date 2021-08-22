package com.example.features.`object`.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class ObjectDirectRequest(
    val name: String,
    val fileUrl: String,
    val fileExtension: String,
    val imageUrl: String
) {
    init {
        Validation<ObjectDirectRequest> {
            ObjectDirectRequest::name required {}
            ObjectDirectRequest::fileUrl required {}
            ObjectDirectRequest::imageUrl required {}
            ObjectDirectRequest::fileExtension required {}
        }.validateAndThrowOnFailure(this)
    }
}

