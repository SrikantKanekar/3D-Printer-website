package com.example.model

import com.example.util.validateAndThrowOnFailure
import io.konform.validation.Validation
import io.konform.validation.jsonschema.maximum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
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
            }
            Address::lastname{
                minLength(2)
            }
            Address::phoneNumber required {
                maximum(9_999_999_999)
                minimum(999_999_999)
            }
            Address::address{

            }
            Address::city{

            }
            Address::state{

            }
            Address::country{

            }
            Address::pinCode required {
                maximum(999999)
                minimum(99999)
            }
        }.validateAndThrowOnFailure(this)
    }
}
