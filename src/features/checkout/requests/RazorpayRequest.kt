package com.example.features.checkout.requests

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class RazorpayRequest(
    val id: String,
    val order_id: String,
    val payment_id: String,
    val signature: String
){
    init {
        Validation<RazorpayRequest> {
            RazorpayRequest::id required {}
            RazorpayRequest::order_id required {}
            RazorpayRequest::payment_id required {}
            RazorpayRequest::signature required {}
        }.validateAndThrowOnFailure(this)
    }
}