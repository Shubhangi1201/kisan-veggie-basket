package com.example.veggiebasket.authentication.presentation.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.user.shopping.MainActivity
import com.example.veggiebasket.R
import com.example.veggiebasket.admin.AdminActivity
import com.example.veggiebasket.authentication.domain.model.UserType
import com.example.veggiebasket.authentication.presentation.viewmodel.AuthViewModel
import com.example.veggiebasket.authentication.domain.utils.Resource
import com.example.veggiebasket.authentication.presentation.util.InputValidations
import com.example.veggiebasket.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private val authViewModel by viewModels<AuthViewModel>()
    private var userType = UserType.USER

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        binding.signupBtn.setOnClickListener{
            val name : String = binding.userName.text.toString()
            val email: String = binding.userEmail.text.toString()
            val password: String = binding.userPassword.text.toString()
            if(email != null && password != null){
                authViewModel?.signupUser(name, email, password, userType)
            }

        }
        binding.apply {
            AdminSignupTV.setOnClickListener{
                if(userType == UserType.USER){
                    userType = UserType.ADMIN
                    SignUpHeadingTV.text = "Seller SignUp"
                    AdminSignupTV.text = "SignUp as User? click here"
                }else{
                    userType = UserType.USER
                    SignUpHeadingTV.text = "User SignUp"
                    AdminSignupTV.text = "SingUp as Seller? click here"
                }
            }
        }


        binding.backToLogin.setOnClickListener{
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
        }

        lifecycleScope.launch {
            authViewModel.signupFlow.collect{resources->
                when(resources){
                    is Resource.Failure -> {
                        Toast.makeText(context, resources.execption.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Toast.makeText(context, "LOADING IN PRORESS", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        when(resources.userType){
                            UserType.ADMIN -> {
                                startActivity(Intent(context, AdminActivity::class.java))
                                activity?.finish()
                            }
                            UserType.USER -> {
                                startActivity(Intent(context, MainActivity::class.java))
                                activity?.finish()

                            }
                        }
                    }
                    null -> {
                        Toast.makeText(context, "null resources", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        lifecycleScope.launch {
            authViewModel.validation.collect{validation ->
                if(validation.email is InputValidations.Failure){
                    binding.userEmail.apply {
                        requestFocus()
                        error = validation.email.message
                    }
                }

                if (validation.password is InputValidations.Failure){
                    binding.userPassword.apply {
                        requestFocus()
                        error = validation.password.message
                    }
                }

            }
        }

        return binding.root
    }

}