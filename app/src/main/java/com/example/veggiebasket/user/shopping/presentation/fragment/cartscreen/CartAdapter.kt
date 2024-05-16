package com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.CartSinglerowBinding
import com.example.veggiebasket.databinding.FragmentCartBinding
import com.example.veggiebasket.databinding.SingleCategoryLayoutBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

class CartAdapter(
    val list: ArrayList<FetchProduct>,
    private val navigateToProductDetailInfo: (FetchProduct) -> Unit,
    private val removeDetail: (FetchProduct) -> Unit

): RecyclerView.Adapter<CartAdapter.myViewHolder>() {

    inner class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = CartSinglerowBinding.bind(itemView)
        fun bind(product: FetchProduct){
            binding.CSRname.text = product.name
            binding.CSRprice.text = "Price: ${product.price}"
            binding.CSRofferpercentage.text = "Offer ${product.offerPercentage}%"
            binding.quantity.text = "Sold by : ${product.sellerName}"
            binding.CSRremove.setOnClickListener{
                removeDetail(product)
            }

            binding.cartBuyNow.setOnClickListener{
                navigateToProductDetailInfo(product)
            }
            val imageUrlMap = product.imageUrl

            // Load the first image URL if it exists
            val firstImageUrl = imageUrlMap.values.firstOrNull()
            if (firstImageUrl != null) {
                Glide.with(itemView)
                    .load(firstImageUrl)
                    .into(binding.imageView2)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = CartSinglerowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return myViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
       val product = list.get(position)
        holder.bind(product)
    }
}