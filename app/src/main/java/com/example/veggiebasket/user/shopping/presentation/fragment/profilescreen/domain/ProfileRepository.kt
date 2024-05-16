package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.domain

import androidx.lifecycle.LiveData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData

interface ProfileRepository {
    suspend fun uploadPersonalData(details: PersonalData): LiveData<CategoryResource<String>>
    suspend fun getPersonalDetails(): LiveData<CategoryResource<PersonalData>>
}