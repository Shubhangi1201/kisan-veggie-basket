package com.example.veggiebasket.authentication.domain.repository

import com.example.veggiebasket.authentication.domain.model.UserType
import com.example.veggiebasket.authentication.domain.utils.Resource
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser : FirebaseUser?
    val userType: UserType?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String, userType: UserType): Resource<FirebaseUser>
    suspend fun saveAdminInfo(name: String, email: String)
    suspend fun fetchAdminInfo(email: String):Boolean
    suspend fun resetPassword(email: String): Resource<String>

    fun logout()

}