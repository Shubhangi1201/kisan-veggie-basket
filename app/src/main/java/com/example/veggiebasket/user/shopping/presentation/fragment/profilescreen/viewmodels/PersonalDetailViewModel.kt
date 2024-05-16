package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels

import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.domain.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalDetailViewModel @Inject constructor(
        private val repository: ProfileRepository
): ViewModel() {



    private val _uploadData = MutableLiveData<CategoryResource<String>>()
    val uploadData: LiveData<CategoryResource<String>> = _uploadData

    private val _getProfileData = MutableLiveData<CategoryResource<PersonalData>>()
    val getProfileData : LiveData<CategoryResource<PersonalData>> = _getProfileData

    fun uploadPersonalData(details: PersonalData){
        viewModelScope.launch {
            repository.uploadPersonalData(details).observeForever { result ->
                _uploadData.value = result
            }
        }
    }

    fun getProfileData(){
        viewModelScope.launch {
            repository.getPersonalDetails().observeForever{result ->
                _getProfileData.value = result
            }
        }
    }
}