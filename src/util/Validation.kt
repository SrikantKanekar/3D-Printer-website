package com.example.util

import io.konform.validation.Invalid
import io.konform.validation.Validation

class ValidationException(message: String) : Exception(message)

fun <T> Validation<T>.validateAndThrowOnFailure(value: T) {
    val result = validate(value)
    if (result is Invalid<T>) {
        throw ValidationException(result.errors.toString())
    }
}