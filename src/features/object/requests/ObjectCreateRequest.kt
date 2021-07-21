package com.example.features.`object`.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class ObjectCreateRequest(
    val id: String,
    val name: String,
    val fileUrl: String,
    val imageUrl: String,
    val fileExtension: String
) {
    init {
        Validation<ObjectCreateRequest> {
            ObjectCreateRequest::id required {}
            ObjectCreateRequest::name required {}
            ObjectCreateRequest::fileUrl required {}
            ObjectCreateRequest::imageUrl required {}
            ObjectCreateRequest::fileExtension required {}
        }.validateAndThrowOnFailure(this)
    }
}

