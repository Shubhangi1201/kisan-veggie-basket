package com.example.veggiebasket.authentication.domain.repository

import android.util.Log
import com.example.veggiebasket.authentication.data.SharedPreferencesManager
import com.example.veggiebasket.authentication.domain.model.User
import com.example.veggiebasket.authentication.domain.model.UserType
import com.example.veggiebasket.authentication.domain.utils.Resource
import com.example.veggiebasket.authentication.domain.utils.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val databaseReference: DatabaseReference
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser
    override val userType: UserType?
        get() = sharedPreferencesManager.getUserType()

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try{
            var user : UserType

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            if(fetchAdminInfo(email)){
                user = UserType.ADMIN
            }else{
                user = UserType.USER
            }
            Resource.Success(result.user!!, user)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(
        name: String,
        email: String,
        password: String,
        userType: UserType
    ): Resource<FirebaseUser> {
        return try{
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            sharedPreferencesManager.saveUserType(userType)
            Resource.Success(result.user!!, userType)
        }catch (e: Exception){
            e.printStackTrace()
            Resource.Failure(e)
        }
    }



    override suspend fun saveAdminInfo(name: String, email: String) {
        val dbref = databaseReference.child("Admin")
        currentUser.let {
            val userkey = emailKey(email)

            dbref.child(userkey).child("personal details").setValue(User(name, email)).addOnCompleteListener{
                if(it.isSuccessful){
                    Log.d("abc", "admin info saved successfully")
                }else{
                    Log.d("abc", "admin info failed to save ${it.exception.toString()}")
                }
            }
        }

    }

    override suspend fun fetchAdminInfo(email: String):Boolean {
        val dbref = databaseReference.child("Admin")
        val userKey = emailKey(email)
        val result = CompletableDeferred<Boolean>()
        dbref.child(userKey).child("personal details").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                   Log.d("abclogin", "welcome admin")
                    sharedPreferencesManager.saveUserType(UserType.ADMIN)
                    result.complete(true)
                }else{
                    Log.d("abclogin", "no admin")
                    sharedPreferencesManager.saveUserType(UserType.USER)
                    result.complete(false)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        return result.await()
    }

    override suspend fun resetPassword(email: String): Resource<String> {
        return try {
            val result = firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success("Password reset email sent successfully", UserType.USER)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }



    override fun logout() {
        firebaseAuth.signOut()
    }

    private fun emailKey(emailAddress: String): String {
        val parts = emailAddress.split("@")
        return parts[0]
    }

}