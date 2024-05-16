package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.veggiebasket.databinding.FragmentEditAddressBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.AddressData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.EditAddressViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.MyAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditAddressFragment: Fragment() {
    private lateinit var binding: FragmentEditAddressBinding
    private val viewmodel by viewModels<EditAddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditAddressBinding.inflate(inflater)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSubmitEditAddress.setOnClickListener{

                val name = binding.editTextName.text.toString()
                val addressLine1 = binding.editTextAddressLine1.text.toString()
                val addressLine2 = binding.editTextAddressLine2.text.toString()
                val city = binding.editTextCity.text.toString()
                val state = binding.editTextState.text.toString()
                val postalCode = binding.editTextPostalCode.text.toString()
                val country = binding.editTextCountry.text.toString()

            if(name.isEmpty() || addressLine1.isEmpty() || addressLine2.isEmpty() || city.isEmpty() || state.isEmpty() || postalCode.isEmpty() || country.isEmpty()){
                Toast.makeText(requireContext(), "Please fill all input filedss", Toast.LENGTH_SHORT).show()
            }else{
                val addressData = AddressData(
                    name,
                    addressLine1,
                    addressLine2,
                    city,
                    state,
                    postalCode,
                    country
                )

                viewmodel.saveAddressToFirebase(
                    addressData,
                    onSuccess = {
                        Toast.makeText(requireContext(), "Address updated successfully", Toast.LENGTH_SHORT).show()
                    },
                    onFailure = {
                        Toast.makeText(requireContext(), "Failed to upload", Toast.LENGTH_SHORT).show()

                    }
                )


            }




        }
    }
}