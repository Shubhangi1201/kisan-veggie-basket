package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.location.Address
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentMyAddressBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.AddressData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.LogoutViewmodel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.MyAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAddressFragment : Fragment() {
    private lateinit var binding: FragmentMyAddressBinding
    private val viewmodel by viewModels<MyAddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMyAddressBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.address.observe(viewLifecycleOwner){resource->
            when(resource){
                is CategoryResource.Error -> {
                    Toast.makeText(requireContext(), "snapshot doesn't exist", Toast.LENGTH_SHORT).show()
                    binding.myaddressbuttonSubmit.text = "Add address"
                }
                is CategoryResource.Loading -> {

                }
                is CategoryResource.Success -> {
                    Toast.makeText(requireContext(), "exist", Toast.LENGTH_SHORT).show()
                    binding.lladdress.visibility = View.VISIBLE
                    binding.myaddressbuttonSubmit.text = "Change address"
                    val data = resource.data
                    setTextData(data)

                }
            }
        }

        binding.myaddressbuttonSubmit.setOnClickListener{
            findNavController().navigate(R.id.action_myAddressFragment_to_editAddressFragment)
        }

    }


    private fun setTextData(data: AddressData){
        binding.apply {
            textViewAddressLine1.text = data.addressLine1
            textViewAddressLine2.text= data.addressLine2
            textViewCity.text = data.city
            textViewPostalCode.text = data.postalCode
            textViewState.text = data.state
            textViewCountry.text = data.country
        }
    }

}