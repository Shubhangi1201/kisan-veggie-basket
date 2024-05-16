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
import com.example.veggiebasket.authentication.presentation.dialog.setupBottomSheetDialog
import com.example.veggiebasket.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding.LoginBtn.setOnClickListener{
            val email: String = binding.LoginEmail.text.toString()
            val password: String = binding.LoginPassword.text.toString()

            if(email != null && password != null){
                authViewModel.loginUser(email, password)
            }
        }

        binding.backToSignup.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        binding.foregetpasswordTV.setOnClickListener{
            setupBottomSheetDialog {email ->
                authViewModel.resetPassword(email)
            }
        }


        lifecycleScope.launch {
            authViewModel.resetPassword.collect{resources ->
                when(resources){
                    is Resource.Failure -> {
                        Snackbar.make(requireView(), "Error : ${resources.execption}", Snackbar.LENGTH_LONG).show()

                    }
                    is Resource.Loading -> {
                        Snackbar.make(requireView(), "loading", Snackbar.LENGTH_LONG).show()

                    }
                    is Resource.Success ->{

                        Snackbar.make(requireView(), "Password reset link has been sent to your email", Snackbar.LENGTH_LONG).show()

                    }
                }
            }
        }

        lifecycleScope.launch {
            authViewModel.loginFlow.collect{resources->
                when(resources){
                    is Resource.Failure -> {
                        Toast.makeText(context, resources.execption.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success ->{

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
                    null ->{
//                        Toast.makeText(context, "null", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        return binding.root
    }
}