package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.veggiebasket.authentication.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogoutViewmodel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {
    val showDialogLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun logout() {
        repository.logout()
    }

    fun showDialog() {
        showDialogLiveData.value = true
    }

    fun hideDialog() {
        showDialogLiveData.value = false
    }
}
