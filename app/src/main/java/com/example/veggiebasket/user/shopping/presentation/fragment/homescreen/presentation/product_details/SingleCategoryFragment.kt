package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.product_details

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentSingleCategoryBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEvent
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEventProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.HomeFragmentDirections
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter.SingleCategoryAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.HomeViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SingleCategoryFragment : Fragment() {
    private lateinit var binding: FragmentSingleCategoryBinding
    private val viewmodel by viewModels<HomeViewModel>()
    private lateinit var CategoryAdapter : SingleCategoryAdapter
    private val viewmodelProductData: ProductDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingleCategoryBinding.inflate(inflater, container, false)



        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryName = arguments?.getString("Categoryname")
        if(categoryName!=null){
            Toast.makeText(context, "${categoryName}", Toast.LENGTH_SHORT).show()
            filterProduct(categoryName)
            binding.textView6.text = categoryName
        }

        viewmodel.navigationEventProductInfo.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { navigationEvent ->
                when (navigationEvent) {
                    is NavigationEventProduct.NavigateToInforFragment -> {
                        val product = navigationEvent.product
                        viewmodelProductData.setProdctData(product)
                        findNavController().navigate(R.id.action_singleCategoryFragment_to_singleProductInfoFragment)
                    }
                }
            }
        }

    }


    private fun filterProduct(categoryName: String){
        lifecycleScope.launch {
            viewmodel.specialProduct.collect{resource ->
                when(resource){
                    is CategoryResource.Error -> {

                    }
                    is CategoryResource.Loading -> {

                    }
                    is CategoryResource.Success -> {
                        val list = resource.data
                        val productList = list.filter { it.category == categoryName }
                        updateUi(productList)

                    }
                }
            }
        }
    }

    private fun updateUi(productList: List<FetchProduct>){
        binding.apply {
            CategoryAdapter = SingleCategoryAdapter(productList){
                viewmodel.onSingleProductClick(it)
            }
            SingleCategoryRV.layoutManager = GridLayoutManager(requireContext(), 3)
            SingleCategoryRV.adapter = CategoryAdapter
        }
    }

}