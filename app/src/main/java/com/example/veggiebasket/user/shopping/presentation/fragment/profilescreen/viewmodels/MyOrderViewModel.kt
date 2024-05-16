package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels

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
class MyOrderViewModel @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
): ViewModel() {

    private lateinit var arrayList: ArrayList<FetchProduct>
    private val _myorderProduct = MutableStateFlow<CategoryResource<ArrayList<FetchProduct>>>(
        CategoryResource.Loading)
    val myorderProduct: MutableStateFlow<CategoryResource<ArrayList<FetchProduct>>> = _myorderProduct

    init {
        fetchOrderProduct()
    }


    fun fetchOrderProduct(){
        arrayList = arrayListOf()
        arrayList.clear()

        val currentUser = auth.currentUser?.uid
        dbref.child("user").child(currentUser!!).child("MyOrder")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(details in snapshot.children){
                            val result = details.getValue(FetchProduct::class.java)
                            arrayList.add(result!!)
                        }
                        _myorderProduct.value = CategoryResource.Success(arrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _myorderProduct.value = CategoryResource.Error(error.message.toString())
                }

            })
    }


}