package com.example.model

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maxLength
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@Serializable
data class Address(
    val firstname: String = "",
    val lastname: String = "",
    val phoneNumber: Long? = null,
    val address: String = "",
    val city: String = "",
    val state: String = "",
    val country: String = "",
    val pinCode: Int? = null
) {
    fun validate() {
        Validation<Address> {
            Address::firstname{
                minLength(2)
                maxLength(50)
            }
            Address::lastname{
                minLength(2)
                maxLength(50)
            }
            Address::phoneNumber required {}
            Address::address required {}
            Address::city required {}
            Address::state required {}
            Address::country required {}
            Address::pinCode required {}
        }.validateAndThrowOnFailure(this)
    }
}
