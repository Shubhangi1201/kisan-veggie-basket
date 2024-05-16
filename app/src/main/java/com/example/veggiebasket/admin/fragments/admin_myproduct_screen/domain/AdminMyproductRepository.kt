package com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain

import androidx.lifecycle.LiveData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource

interface AdminMyproductRepository {
    fun getMyProducts(): LiveData<CategoryResource<ArrayList<FetchProduct>>>
}