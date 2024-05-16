package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.AddressData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyAddressViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val dbref: DatabaseReference
): ViewModel() {
    val address : MutableLiveData<CategoryResource<AddressData>> = MutableLiveData()

    init {
        checkAddressInDb()
    }

    private fun checkAddressInDb(){
        val currentUser = auth.currentUser?.uid.toString()
        dbref.child("user").child(currentUser).child("MyAddress")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        address.value = CategoryResource.Success(snapshot.getValue(AddressData::class.java)!!)
                    }else{
                        address.value = CategoryResource.Error("Snapshot doesn't exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}