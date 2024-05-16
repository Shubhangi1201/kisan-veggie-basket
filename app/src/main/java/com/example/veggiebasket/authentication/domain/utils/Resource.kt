package com.example.veggiebasket.authentication.domain.utils

import com.example.veggiebasket.authentication.domain.model.UserType

sealed class Resource<out R>{
    data class Success<out R>(val result: R, val userType: UserType): Resource<R>()
    data class Failure(val execption : Exception): Resource<Nothing>()

    object Loading: Resource<Nothing>()
}
