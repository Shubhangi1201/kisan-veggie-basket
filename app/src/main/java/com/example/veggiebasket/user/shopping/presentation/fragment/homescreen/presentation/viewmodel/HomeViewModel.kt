package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.CategoryData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEvent
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEventProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository.CategoryRepository
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CategoryRepository
) : ViewModel(){
//    private val _categories = MutableStateFlow<List<CategoryData>>(emptyList())
//    val categories = _categories

    private val _categories = MutableStateFlow<CategoryResource<List<CategoryData>>>(
        CategoryResource.Loading)
    val categories: MutableStateFlow<CategoryResource<List<CategoryData>>> = _categories

    private val _specialProduct = MutableStateFlow<CategoryResource<List<FetchProduct>>>(
        CategoryResource.Loading)
    val specialProduct: MutableStateFlow<CategoryResource<List<FetchProduct>>> = _specialProduct


    private val _navigationEvent = MutableLiveData<Event<NavigationEvent>>()
    val navigationEvent: LiveData<Event<NavigationEvent>> get() = _navigationEvent

    private val _navigationEventProductInfo = MutableLiveData<Event<NavigationEventProduct>>()
    val navigationEventProductInfo: LiveData<Event<NavigationEventProduct>> get() = _navigationEventProductInfo


    init {
        fetchCategories()
        fetchSpecialProducts()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = repository.getCategory().collect{result ->
                    _categories.value = CategoryResource.Success(result)
                }
            } catch (e: Exception) {
                // Handle error
                _categories.value = CategoryResource.Error(e.message.toString())
            }
        }
    }

    private fun fetchSpecialProducts() {
        viewModelScope.launch {
            try {
                val result = repository.getSpecialProduct().collect{result ->
                    _specialProduct.value = CategoryResource.Success(result)
                    Log.e("abcedefgh", result.toString())
                }
            } catch (e: Exception) {
                // Handle error
                _specialProduct.value = CategoryResource.Error(e.message.toString())
                Log.e("abcedefgh", e.message.toString())

            }
        }
    }

    fun onCategoryItemClick(name: String) {
        _navigationEvent.value = Event(NavigationEvent.NavigateToNextFragment(name))
    }

    fun onSingleProductClick(product: FetchProduct){
        _navigationEventProductInfo.value = Event(NavigationEventProduct.NavigateToInforFragment(product))
    }

}