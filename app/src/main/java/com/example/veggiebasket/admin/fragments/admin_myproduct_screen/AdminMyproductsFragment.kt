package com.example.veggiebasket.admin.fragments.admin_myproduct_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.veggiebasket.R
import com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain.AdminMyProductViewModel
import com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain.AdminProductAdapter
import com.example.veggiebasket.databinding.FragmentAdminMyproductsBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.orderscreen.MyOrderMainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminMyproductsFragment : Fragment() {
    private lateinit var binding: FragmentAdminMyproductsBinding
    private  val viewmodel by viewModels<AdminMyProductViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdminMyproductsBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your Products"
        viewmodel.getProductDetails()
        viewmodel.adminProduct.observe(viewLifecycleOwner){resource->
            when(resource){
                is CategoryResource.Error ->{

                }
                is CategoryResource.Loading -> {

                }
                is CategoryResource.Success -> {
                    Log.d("qwert", resource.data.toString())
                    updateUi(resource.data)
                }
            }
        }
    }

    fun updateUi(list: ArrayList<FetchProduct>){
        binding.adminProductRV.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = AdminProductAdapter(
                list,
                editProduct = {
                    editProductDetail(it)
                },
                removeProduct = {
                    removeProductDetail(it)
                }
            )
        }
    }

    fun editProductDetail(product: FetchProduct){
            findNavController().navigate(R.id.action_adminMyproductsFragment2_to_adminEditProductFragment)
    }

    fun removeProductDetail(product: FetchProduct){

    }
}