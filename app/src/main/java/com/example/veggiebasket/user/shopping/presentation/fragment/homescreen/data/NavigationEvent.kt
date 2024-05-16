package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data

// NavigationEvent.kt
sealed class NavigationEvent {
    data class NavigateToNextFragment(val categoryName: String) : NavigationEvent()
}
