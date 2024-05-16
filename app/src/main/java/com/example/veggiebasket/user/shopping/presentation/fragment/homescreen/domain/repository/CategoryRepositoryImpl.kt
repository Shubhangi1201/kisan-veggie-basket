package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.repository

import android.util.Log
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.CategoryData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class CategoryRepositoryImpl @Inject constructor(
   private val databaseRef: DatabaseReference,
   private val auth: FirebaseAuth
): CategoryRepository {
    private val _categoryFlow = MutableStateFlow<List<CategoryData>>(emptyList())
    override suspend fun getCategory(): Flow<List<CategoryData>> = _categoryFlow


    private val _specialProduct = MutableStateFlow<List<FetchProduct>>(emptyList())
    override suspend fun getSpecialProduct(): Flow<List<FetchProduct>>  = _specialProduct



    init {
        fetchCategories()
        fetchSpecialProducts()
    }

    fun fetchCategories(){
        databaseRef.child("Categories").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val categories = mutableListOf<CategoryData>()
                for (childSnapshot in snapshot.children){
                    val det = childSnapshot.getValue(CategoryData::class.java)
                    det?.let {
                        categories.add(it)
                    }
                }
                // Update the MutableStateFlow with the new list of categories
                _categoryFlow.value = categories
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })

    }

    fun fetchSpecialProducts() {
        databaseRef.child("Admin").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val product = mutableListOf<FetchProduct>()

                for (userSnapshot in snapshot.children) {
                    // Assuming "ProductDetails" is the key for product details node
                    val productDetailsSnapshot = userSnapshot.child("MyProducts")

                    for (uniqueKeySnapshot in productDetailsSnapshot.children) {
                        val singleproduct = uniqueKeySnapshot.getValue(FetchProduct::class.java)
                        singleproduct.let {
                            product.add(singleproduct!!)
                            Log.d("abc", product.toString())
                        }
                    }
                }

                _specialProduct.value = product
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
    }


    override suspend fun addToCart(cartItem: FetchProduct): CategoryResource<String> {
        val currentUser = auth.currentUser?.uid.toString()

        val dbkey = databaseRef.child("user").child(currentUser).child("MyCart").push().key.toString()
        // Use a suspendCoroutine to convert the Firebase API callback to a suspend function

        val modifiedCardItem = cartItem.copy(id = dbkey)
        return kotlin.coroutines.suspendCoroutine { continuation ->
            databaseRef.child("user").child(currentUser).child("MyCart")
                .child(dbkey).setValue(modifiedCardItem)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(CategoryResource.Success("Product Added to cart"))
                    } else {
                        task.exception?.let { exception ->
                            continuation.resume(CategoryResource.Error(exception.message.toString()))
                        }
                    }
                }
        }
    }

    override suspend fun myOrder(orderItem: MyOrderData): CategoryResource<String> {
        val currentUser = auth.currentUser?.uid.toString()

        // Use a suspendCoroutine to convert the Firebase API callback to a suspend function
        return kotlin.coroutines.suspendCoroutine { continuation ->
            databaseRef.child("user").child(currentUser).child("MyOrder")
                .push().setValue(orderItem)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(CategoryResource.Success("Product Added to cart"))
                    } else {
                        task.exception?.let { exception ->
                            continuation.resume(CategoryResource.Error(exception.message.toString()))
                        }
                    }
                }
        }
    }

    override suspend fun removeFromCart(cartItemId: String, callback: (Boolean) -> Unit): CategoryResource<String> {
        val currentUser = auth.currentUser?.uid.toString()

        // Use a suspendCoroutine to convert the Firebase API callback to a suspend function
        return kotlin.coroutines.suspendCoroutine { continuation ->
            databaseRef.child("user").child(currentUser).child("MyCart").child(cartItemId)
                .removeValue()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(true)
                        Log.d("cart", "item removed succesfully")
                        continuation.resume(CategoryResource.Success("Product removed from cart"))
                    } else {
                        task.exception?.let { exception ->
                            Log.d("cart", "item failedfailed to remove")
                            callback(true)
                            continuation.resume(CategoryResource.Error(exception.message.toString()))
                        }
                    }
                }
        }
    }

}