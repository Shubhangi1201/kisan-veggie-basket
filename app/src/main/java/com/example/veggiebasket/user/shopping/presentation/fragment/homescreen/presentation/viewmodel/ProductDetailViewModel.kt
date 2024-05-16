package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veggiebasket.authentication.domain.repository.AuthRepository
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEvent
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository.CategoryRepository
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {

    private val _productData = MutableLiveData<FetchProduct>()
    val prductData: LiveData<FetchProduct> get() = _productData

    private val _AdcResult = MutableLiveData<CategoryResource<String>>()
    val AdcResult: LiveData<CategoryResource<String>> get() = _AdcResult


    private val _orderResult = MutableLiveData<CategoryResource<String>>()
    val orderResult: LiveData<CategoryResource<String>> get() = _orderResult

    private val _removeData = MutableLiveData<CategoryResource<String>>()
    val removeData : MutableLiveData<CategoryResource<String>> = _removeData





    fun setProdctData(product: FetchProduct){
        _productData.value = product
    }

    fun addToCart(product: FetchProduct) = viewModelScope.launch {
        _AdcResult.value = repository.addToCart(product)
    }

    fun myOrder(product: MyOrderData) = viewModelScope.launch{
        _orderResult.value = repository.myOrder(product)
    }

    fun removeData(orderItemId: String, callback : (Boolean)->Unit) = viewModelScope.launch {
        _removeData.value = repository.removeFromCart(orderItemId){
            if(true){
                callback(true)
            }
        }
    }

}