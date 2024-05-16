package com.example.veggiebasket.user.shopping.presentation.fragment.orderscreen

import androidx.lifecycle.ViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
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
class MyOrderMainViewModel @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
): ViewModel() {

    private lateinit var arrayList: ArrayList<MyOrderData>
    private val _orderProduct = MutableStateFlow<CategoryResource<ArrayList<MyOrderData>>>(
        CategoryResource.Loading)
    val orderProduct: MutableStateFlow<CategoryResource<ArrayList<MyOrderData>>> = _orderProduct


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
                            val result = details.getValue(MyOrderData::class.java)
                            arrayList.add(result!!)
                        }
                        _orderProduct.value = CategoryResource.Success(arrayList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    _orderProduct.value = CategoryResource.Error(error.message.toString())
                }

            })
    }



}