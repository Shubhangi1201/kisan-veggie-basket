package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data

// NavigationEvent.kt
sealed class NavigationEventProduct {
    data class NavigateToInforFragment(val product: FetchProduct): NavigationEventProduct()
}
