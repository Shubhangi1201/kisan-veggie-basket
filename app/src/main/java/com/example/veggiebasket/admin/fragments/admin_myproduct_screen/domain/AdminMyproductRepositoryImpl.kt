package com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class AdminMyproductRepositoryImpl @Inject constructor(
    private val dbref: DatabaseReference,
    private val auth: FirebaseAuth
) : AdminMyproductRepository {
    override fun getMyProducts(): LiveData<CategoryResource<ArrayList<FetchProduct>>> {
        val resultLiveData = MutableLiveData<CategoryResource<ArrayList<FetchProduct>>>()

        val currentUser= auth.currentUser?.email.toString()
        val emailkey = emailKey(currentUser)

        dbref.child("Admin").child(emailkey).child("MyProducts")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val arraylist: ArrayList<FetchProduct> = arrayListOf()
                    if(snapshot.exists()){
                        for (det in snapshot.children){
                            val detail = det.getValue(FetchProduct::class.java)
                            arraylist.add(detail!!)
                        }

                        resultLiveData.value = CategoryResource.Success(arraylist)
                    }else{
                        resultLiveData.value = CategoryResource.Error("Product details are empty")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        return resultLiveData
    }


    private fun emailKey(emailAddress: String): String {
        val parts = emailAddress.split("@")
        return parts[0]
    }
}