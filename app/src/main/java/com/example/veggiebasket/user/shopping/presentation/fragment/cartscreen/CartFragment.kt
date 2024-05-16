package com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentCartBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CartFragment: Fragment() {
    private lateinit var binding: FragmentCartBinding
    private  val viewmodel by viewModels<CartViewModel>()
    private val viewmodelproductdetail: ProductDetailViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your Basket"
        fetchCartProduct()

    }

    private fun updateUi(list: ArrayList<FetchProduct>){
        binding.apply {
            cartRV.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = CartAdapter(
                    list,
                    navigateToProductDetailInfo = {
                        onBuyNowClick(it)
                    },
                    removeDetail = {
                        onRemoveClick(it)
                    }
                )
            }
        }
    }

    fun onBuyNowClick(product: FetchProduct) {
        viewmodelproductdetail.setProdctData(product)
        findNavController().navigate(R.id.action_cartFragment_to_singleProductInfoFragment)
    }

    fun onRemoveClick(product: FetchProduct) {
        viewmodelproductdetail.removeData(product.id){
            if(true){
                fetchCartProduct()
            }
        }

    }

    private fun fetchCartProduct(){
        Log.d("cart", "fetchProductMethod is being called")

        lifecycleScope.launch {
            viewmodel.cartProduct.collect{resource->
                when(resource){
                    is CategoryResource.Error -> {

                    }
                    is CategoryResource.Loading -> {

                    }
                    is CategoryResource.Success -> {
                        updateUi(resource.data)
                        Log.d("cart", "$")
                    }
                }
            }
        }
    }

}