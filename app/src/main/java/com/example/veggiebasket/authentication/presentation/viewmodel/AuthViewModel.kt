package com.example.veggiebasket.authentication.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.veggiebasket.authentication.domain.model.UserType
import com.example.veggiebasket.authentication.domain.repository.AuthRepository
import com.example.veggiebasket.authentication.domain.utils.Resource
import com.example.veggiebasket.authentication.presentation.util.InputFieldState
import com.example.veggiebasket.authentication.presentation.util.InputValidations
import com.example.veggiebasket.authentication.presentation.util.ValidateEmail
import com.example.veggiebasket.authentication.presentation.util.ValidatePassword
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val databaseReference: DatabaseReference
): ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val loginFlow : StateFlow<Resource<FirebaseUser>?> = _loginFlow

    private val _signupFlow = MutableStateFlow<Resource<FirebaseUser>?>(null)
    val signupFlow : StateFlow<Resource<FirebaseUser>?> = _signupFlow

    private val _validation = Channel<InputFieldState>()
    val validation = _validation.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()


    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null){
            _loginFlow.value = Resource.Success(repository.currentUser!!, repository.userType!!)
        }
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
         _loginFlow.value = Resource.Loading
        var result : Resource<FirebaseUser> = repository.login(email, password)

        _loginFlow.value = result
    }


    fun signupUser(name: String, email: String, password: String, userType: UserType) = viewModelScope.launch {
        if(CheckValidation(email, password)){
            var result: Resource<FirebaseUser>
            _signupFlow.value = Resource.Loading
            if(userType == UserType.ADMIN){
                result = repository.signup(name, email, password, userType)
                if(result is Resource.Success<FirebaseUser>){
                    Log.d("abc", "resource.success i am inside viewmodel auth viewmodel signupmethod")
                    repository.saveAdminInfo(name, email)
                }else{
                    Log.d("abc", "resource.failure i am inside viewmodel auth viewmodel signupmethod")
                }
            }else{
              result = repository.signup(name, email, password, userType)
            }
            _signupFlow.value = result
        }else{
            val inputFieldState = InputFieldState(
                ValidateEmail(email), ValidatePassword(password)
            )
            _validation.send(inputFieldState)
        }
    }

    fun logout(){
        repository.logout()
        _loginFlow.value = null
        _signupFlow.value = null
    }


    private fun CheckValidation(email: String, password: String): Boolean{
        val emailValidation = ValidateEmail(email)
        val passwordValidation = ValidatePassword(password)
        val shouldValidate = emailValidation is InputValidations.Success &&
                                passwordValidation is InputValidations.Success

        return shouldValidate
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resetPassword.emit(Resource.Loading)
        val result = repository.resetPassword(email)
        _resetPassword.emit(result)
    }






}