package com.example.veggiebasket.authentication.presentation.util

sealed class InputValidations {
    object Success: InputValidations()
    data class Failure(val message: String): InputValidations()
}

data class InputFieldState(
    val email: InputValidations,
    val password: InputValidations
)