package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data

data class FetchProduct(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val category: String="",
    val price: String = "",
    val offerPercentage: String = "",
    val imageUrl: Map<String, String> = emptyMap(),
    val sellerName: String = "",
    val weight: String=""
)

