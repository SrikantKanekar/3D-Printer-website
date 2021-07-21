package com.example.features.admin.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequest(
    val email: String,
    val subject: String,
    val body: String
){
    init {
        Validation<NotificationRequest> {
            NotificationRequest::email required {}
            NotificationRequest::subject required {}
            NotificationRequest::body required {}
        }.validateAndThrowOnFailure(this)
    }
}
