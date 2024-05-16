package com.example.veggiebasket.authentication.presentation.util

import android.util.Patterns

fun ValidateEmail(email: String): InputValidations{
    if(email.isEmpty()){
        return InputValidations.Failure("Email cannot be empty")
    }
    if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
        return InputValidations.Failure("Invalid email format")
    }

    return InputValidations.Success
}

fun ValidatePassword(password: String): InputValidations{
    if(password.isEmpty()){
        return InputValidations.Failure("Password cannot be empty")
    }
    if(password.length < 6){
        return InputValidations.Failure("Password must contain minimum 6 characters")
    }

    return InputValidations.Success
}