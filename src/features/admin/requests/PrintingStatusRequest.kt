package com.example.features.admin.requests

import com.example.util.enums.PrintingStatus
import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class PrintingStatusRequest(
    val orderId: String,
    val objectId: String,
    val printingStatus: PrintingStatus,
) {
    init {
        Validation<PrintingStatusRequest> {
            PrintingStatusRequest::orderId required {}
            PrintingStatusRequest::objectId required {}
            PrintingStatusRequest::printingStatus required {}
        }.validateAndThrowOnFailure(this)
    }
}
