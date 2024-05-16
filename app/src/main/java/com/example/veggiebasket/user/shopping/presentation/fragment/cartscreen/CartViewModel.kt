package com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
): ViewModel() {
    private lateinit var arrayList: ArrayList<FetchProduct>
    private val _cartProduct = MutableStateFlow<CategoryResource<ArrayList<FetchProduct>>>(
        CategoryResource.Loading)
    val cartProduct: MutableStateFlow<CategoryResource<ArrayList<FetchProduct>>> = _cartProduct



    init {
        fetchCartProduct()
    }

    fun fetchCartProduct(){
        arrayList = arrayListOf()
        arrayList.clear()
        _cartProduct.value = CategoryResource.Loading

        val currentUser = auth.currentUser?.uid
        dbref.child("user").child(currentUser!!).child("MyCart")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    arrayList.clear()
                    if(snapshot.exists()){
                        for(details in snapshot.children){
                            val result = details.getValue(FetchProduct::class.java)
                            arrayList.add(result!!)
                        }
                        _cartProduct.value = CategoryResource.Success(arrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                   _cartProduct.value = CategoryResource.Error(error.message.toString())
                }

            })
    }


}