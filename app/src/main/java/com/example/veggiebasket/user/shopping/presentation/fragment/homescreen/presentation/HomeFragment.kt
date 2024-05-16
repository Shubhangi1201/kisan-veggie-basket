package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentHomeBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.NavigationEvent
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter.HomeAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter.SpecialProductAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.HomeViewModel
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment: Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeAdapter : HomeAdapter
    private lateinit var specialAdapter: SpecialProductAdapter
    private val viewmodelProductData: ProductDetailViewModel by activityViewModels()

    private val viewmodel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Kisan Veggie Basket"

        homeAdapter = HomeAdapter {categoryName->
            viewmodel.onCategoryItemClick(categoryName)
        }
        SetUpCategoryRV()

        viewmodel.navigationEvent.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { navigationEvent ->
                when (navigationEvent) {
                    is NavigationEvent.NavigateToNextFragment -> {
                        val categoryName = navigationEvent.categoryName

                        val action = HomeFragmentDirections.actionHomeFragmentToSingleCategoryFragment(categoryName)
                        findNavController().navigate(action)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewmodel.categories.collect{resource ->
                when(resource){
                    is CategoryResource.Error -> {
                        Toast.makeText(context, "failure ${resource.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Loading -> {
                        Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Success -> {
                        homeAdapter.differ.submitList(resource.data)
//                        Toast.makeText(context, "Success ${resource.data.toString()}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }


        lifecycleScope.launch {
            viewmodel.specialProduct.collect{resource ->
                when(resource){
                    is CategoryResource.Error -> {
                        Toast.makeText(context, "failure ${resource.message.toString()}", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Loading -> {
                        Toast.makeText(context, "loading", Toast.LENGTH_SHORT).show()
                    }
                    is CategoryResource.Success -> {
//                        homeAdapter.differ.submitList(resource.data)
                        specialAdapter.differ1.submitList(resource.data)
                        Log.d("list", resource.data.toString())
//                        Toast.makeText(context, "Success ${resource.data.toString()}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }

    private fun SetUpCategoryRV(){

        binding.categoryRV.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = homeAdapter
        }

        specialAdapter = SpecialProductAdapter(){
            viewmodelProductData.setProdctData(it)
            findNavController().navigate(R.id.action_homeFragment_to_singleProductInfoFragment)
        }
        binding.SpecialProductRV.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter =specialAdapter
        }
    }
}
