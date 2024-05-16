package com.example.veggiebasket.user.shopping.presentation.fragment.orderscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.veggiebasket.R
import com.example.veggiebasket.databinding.FragmentMyOrderMainBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen.CartAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.domain.util.CategoryResource
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.viewmodel.ProductDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MyOrderMainFragment : Fragment() {
    private lateinit var binding: FragmentMyOrderMainBinding
    private  val viewmodel by viewModels<MyOrderMainViewModel>()
    private val viewmodelproductdetail: ProductDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =  FragmentMyOrderMainBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Your Order"

        lifecycleScope.launch {
            viewmodel.orderProduct.collect{resource->
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

    private fun updateUi(list: ArrayList<MyOrderData>){
        binding.apply {
            orderRV.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = MyOrderMainAdapter(list){
                    val product = FetchProduct(
                        it.id,
                        it.name,
                        it.description,
                        it.category,
                        it.price,
                        it.offerPercentage,
                        it.imageUrl,
                        it.sellerName,
                        it.weight
                    )
                    viewmodelproductdetail.setProdctData(product)
                    findNavController().navigate(R.id.action_myorderMain2fragment_to_product_infor_fragmnet)
                }
            }
        }
    }

}