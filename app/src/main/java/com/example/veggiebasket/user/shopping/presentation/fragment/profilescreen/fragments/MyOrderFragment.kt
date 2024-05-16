package com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentMyOrderBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen.CartViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.profilescreen.viewmodels.MyOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyOrderFragment : Fragment() {
    private lateinit var binding: FragmentMyOrderBinding
    private  val viewmodel by viewModels<MyOrderViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyOrderBinding.inflate(inflater)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
           viewmodel.myorderProduct.collect{resource->
               when(resource){
                   is CategoryResource.Error -> {

                   }
                   is CategoryResource.Loading -> {

                   }
                   is CategoryResource.Success -> {
                       updateUi(resource.data)
                   }
               }
           }
        }
    }
    
    
    private fun updateUi(list: ArrayList<FetchProduct>){
        Toast.makeText(requireContext(), "list is ${list.toString()}", Toast.LENGTH_SHORT).show()
    }

}