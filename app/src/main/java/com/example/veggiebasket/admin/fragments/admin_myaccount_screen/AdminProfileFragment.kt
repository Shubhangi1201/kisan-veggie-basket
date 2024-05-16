package com.example.veggiebasket.admin.fragments.admin_myaccount_screen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentAdminProfileBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.PersonalData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.LogoutViewmodel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.PersonalDetailViewModel


class AdminProfileFragment : Fragment() {
    private lateinit var binding: FragmentAdminProfileBinding
    private val logoutVM by viewModels<LogoutViewmodel>()
    private val viewmodel: PersonalDetailViewModel by activityViewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       binding = FragmentAdminProfileBinding.inflate(inflater)


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

            binding.apply {
                editPersonalDetails11.setOnClickListener{
                    findNavController().navigate(R.id.action_adminProfileFragment2_to_adminnPersonalDetailsFragment)
                }

                privacypolicy11.setOnClickListener{
                    findNavController().navigate(R.id.action_adminProfileFragment2_to_privacyPolicyFragment2)
                }

                termsandconditions11.setOnClickListener{
                    findNavController().navigate(R.id.action_adminProfileFragment2_to_termsFragment2)
                }
                consMyorders11.setOnClickListener{
                    findNavController().navigate(R.id.action_adminProfileFragment2_to_hilt_MyOrderMainFragment)
                }
                consMyaddress11.setOnClickListener{
                    findNavController().navigate(R.id.action_adminProfileFragment2_to_adminMyAddressFragment)
                }
                constLogout11.setOnClickListener{
                    logoutVM.showDialog()
                }
            }
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUi(details: PersonalData){
        binding.apply {
            profileName11.text= "Hello, ${details.name}"
            profileEmail11.text = "Email : ${details.email}"
            profileNumber11.text =  "Phone : ${details.phone}"
        }
    }


}