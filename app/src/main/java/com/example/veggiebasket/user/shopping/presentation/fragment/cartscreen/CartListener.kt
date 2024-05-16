package com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen

import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

interface CartListener {
    fun onBuyNowClick()
    fun onRemoveClick(product: FetchProduct)
}