package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels

import androidx.lifecycle.ViewModel

import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.AddressData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditAddressViewModel @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
) : ViewModel() {

    fun saveAddressToFirebase(addressData: AddressData, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val currentUser = auth.currentUser?.uid.toString()
        dbref.child("user").child(currentUser).child("MyAddress")
            .setValue(addressData)
            .addOnSuccessListener { onSuccess.invoke() }
            .addOnFailureListener { onFailure.invoke() }
    }
}