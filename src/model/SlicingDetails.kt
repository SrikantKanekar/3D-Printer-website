package com.example.model

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import kotlinx.serialization.Serializable

@Serializable
data class SlicingDetails(
    val printTime: String? = null,
    val materialWeight: Float? = null,
    val materialCost: Float? = null,
    val powerCost: Float? = null,
    val labourCost: Float? = null,
    val price: Int? = null
) {
    fun validate() {
        Validation<SlicingDetails> {
            SlicingDetails::printTime required {}
            SlicingDetails::materialWeight required {}
            SlicingDetails::materialCost required {}
            SlicingDetails::powerCost required {}
            SlicingDetails::labourCost required {}
            SlicingDetails::price required {}
        }.validateAndThrowOnFailure(this)
    }
}
