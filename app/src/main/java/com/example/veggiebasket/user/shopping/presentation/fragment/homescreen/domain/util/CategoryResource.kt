package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util

sealed class CategoryResource<out T> {
    data class Success<out T>(val data: T) : CategoryResource<T>()
    data class Error(val message: String) : CategoryResource<Nothing>()
    object Loading : CategoryResource<Nothing>()
}