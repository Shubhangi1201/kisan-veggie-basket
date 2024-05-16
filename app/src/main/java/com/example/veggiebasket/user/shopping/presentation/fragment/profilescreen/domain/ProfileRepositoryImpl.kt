package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.veggiebasket.authentication.domain.utils.Resource
import com.example.veggiebasket.authentication.domain.utils.await
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException

class ProfileRepositoryImpl @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
) : ProfileRepository {
    override suspend fun uploadPersonalData(details: PersonalData): LiveData<CategoryResource<String>> {
        val resultLiveData = MutableLiveData<CategoryResource<String>>()

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val currentUserUid = currentUser.uid
            val profileRef = dbref.child("user").child(currentUserUid).child("profile")

            profileRef.setValue(details)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        resultLiveData.value = CategoryResource.Success("Details saved successfully")
                    } else {
                        val exception = task.exception ?: Exception("Unknown error")
                        resultLiveData.value = CategoryResource.Error("Error: ${exception.message}")
                    }
                }
        } else {
            resultLiveData.value = CategoryResource.Error("User not authenticated")
        }

        return resultLiveData
    }

    override suspend fun getPersonalDetails(): LiveData<CategoryResource<PersonalData>> {
        val resultLiveData = MutableLiveData<CategoryResource<PersonalData>>()
        val currentUser = auth.currentUser?.uid

        dbref.child("user").child(currentUser!!).child("profile")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val details = snapshot.getValue(PersonalData::class.java)
                        resultLiveData.value = CategoryResource.Success(details!!)
                    }else{
                        resultLiveData.value = CategoryResource.Error("Personal details not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        return resultLiveData
    }
}