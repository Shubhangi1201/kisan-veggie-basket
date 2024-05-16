package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository

import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.CategoryData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getCategory(): Flow<List<CategoryData>>
    suspend fun getSpecialProduct(): Flow<List<FetchProduct>>
    suspend fun addToCart(cartItem: FetchProduct): CategoryResource<String>
    suspend fun myOrder(orderItem: MyOrderData): CategoryResource<String>
    suspend fun removeFromCart(
        cartItemId: String,
        callback: (Boolean) -> Unit
    ): CategoryResource<String>
}