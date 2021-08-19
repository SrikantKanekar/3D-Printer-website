package com.example.features.`object`.requests

import com.example.model.Slicing
import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class ObjectCreateRequest(
    val id: String,
    val name: String,
    val fileUrl: String,
    val imageUrl: String,
    val fileExtension: String,
    val slicing: Slicing
) {
    init {
        Validation<ObjectCreateRequest> {
            ObjectCreateRequest::id required {}
            ObjectCreateRequest::name required {}
            ObjectCreateRequest::fileUrl required {}
            ObjectCreateRequest::imageUrl required {}
            ObjectCreateRequest::fileExtension required {}
            ObjectCreateRequest::slicing required {}
        }.validateAndThrowOnFailure(this)
    }
}

