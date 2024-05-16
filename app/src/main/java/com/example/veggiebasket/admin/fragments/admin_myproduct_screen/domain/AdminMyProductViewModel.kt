package com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminMyProductViewModel @Inject constructor(
    private val repository: AdminMyproductRepository
): ViewModel() {
    private val _adminProduct= MutableLiveData<CategoryResource<ArrayList<FetchProduct>>>()
    val adminProduct : LiveData<CategoryResource<ArrayList<FetchProduct>>> = _adminProduct

    fun getProductDetails(){
        repository.getMyProducts().observeForever{
            _adminProduct.value = it
        }
    }
}