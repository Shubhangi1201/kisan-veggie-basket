package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentProfileBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.HomeViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.LogoutViewmodel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.PersonalDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment: Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val logoutVM by viewModels<LogoutViewmodel>()
    private val viewmodel: PersonalDetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)
        
        
        return binding.root
               
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your Account"
        viewmodel.getProfileData()
        viewmodel.getProfileData.observeForever{resource->
            when(resource){
                is CategoryResource.Error -> {
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()

                }
                is CategoryResource.Loading -> {

                }
                is CategoryResource.Success -> {
                    val details = resource.data
                    updateUi(details)
                }
            }
        }

        logoutVM.showDialogLiveData.observe(viewLifecycleOwner, Observer { showDialog ->
            if (showDialog) {
                showLogoutDialog()
            } else {
                // Handle dialog dismissal if necessary
            }
        })
        
        binding.apply {
            editPersonalDetails.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_personalDetailsFragment)
            }



            consMyorders.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_myOrderFragment)
            }
            
            consMyaddress.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_myAddressFragment)
            }
            
            privacypolicy.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_privacyPolicyFragment)
            }
            
            termsandconditions.setOnClickListener{
                findNavController().navigate(R.id.action_profileFragment_to_termsFragment)
            }
            
            constLogout.setOnClickListener{
                logoutVM.showDialog()
            }



            
            
        }
    }


    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Do you really want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Handle logout
                logoutVM.logout()
                logoutVM.hideDialog()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Dismiss the dialog
                dialog.dismiss()
                logoutVM.hideDialog()
            }
            .setCancelable(false)
            .show()
    }

    private fun updateUi(details: PersonalData){
        binding.apply {
            profileName.text= "Hello, ${details.name}"
            profileEmail.text = "Email : ${details.email}"
            profileNumber.text =  "Phone : ${details.phone}"
        }
    }
}