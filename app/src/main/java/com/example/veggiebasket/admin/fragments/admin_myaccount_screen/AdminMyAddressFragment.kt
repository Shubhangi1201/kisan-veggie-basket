package com.example.veggiebasket.admin.fragments.admin_myaccount_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.veggiebasket.databinding.FragmentAdminMyAddressBinding
import com.example.veggiebasket.databinding.FragmentMyAddressBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.data.AddressData
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.MyAddressViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMyAddressFragment : Fragment() {
    private lateinit var binding: FragmentAdminMyAddressBinding
    private val viewmodel by viewModels<MyAddressViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminMyAddressBinding.inflate(inflater)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel.address.observe(viewLifecycleOwner){resource->
            when(resource){
                is CategoryResource.Error -> {
                    Toast.makeText(requireContext(), "snapshot doesn't exist", Toast.LENGTH_SHORT).show()
                    binding.myaddressbuttonSubmit22.text = "Add address"
                }
                is CategoryResource.Loading -> {

                }
                is CategoryResource.Success -> {
                    Toast.makeText(requireContext(), "exist", Toast.LENGTH_SHORT).show()
                    binding.lladdress22.visibility = View.VISIBLE
                    binding.myaddressbuttonSubmit22.text = "Change address"
                    val data = resource.data
                    setTextData(data)

                }
            }
        }
    }

    private fun setTextData(data: AddressData){
        binding.apply {
            textViewAddressLine122.text = data.addressLine1
            textViewAddressLine222.text= data.addressLine2
            textViewCity22.text = data.city
            textViewPostalCode22.text = data.postalCode
            textViewState22.text = data.state
            textViewCountry22.text = data.country
        }
    }
}