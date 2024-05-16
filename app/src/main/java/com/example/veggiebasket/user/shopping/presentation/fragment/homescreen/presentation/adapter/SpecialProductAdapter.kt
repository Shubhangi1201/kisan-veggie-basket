package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.SpecialRvItemBinding

import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.CategoryData
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

class SpecialProductAdapter(
    private val navigateToProductDetail: (FetchProduct) -> Unit
):
    RecyclerView.Adapter<SpecialProductAdapter.SpecialProductViewHolder>(){


    inner class SpecialProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SpecialRvItemBinding.bind(itemView)

        fun bind(fetchProduct: FetchProduct) {
            // Bind data to the UI elements in the ViewHolder
            binding.tvSpecialProductName.text = fetchProduct.name

            val imageUrlMap = fetchProduct.imageUrl

            // Load the first image URL if it exists
            val firstImageUrl = imageUrlMap.values.firstOrNull()
            if (firstImageUrl != null) {
                Glide.with(itemView)
                    .load(firstImageUrl)
                    .into(binding.imageSpecialRvItem)
            }
            binding.apply {
                tvSpecialProductName.text = fetchProduct.name
                tvSpecialPrdouctPrice.text= "₹${fetchProduct.price}"
                btnAddToCart.setOnClickListener {
                    navigateToProductDetail(fetchProduct)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductViewHolder {
        val binding = SpecialRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SpecialProductViewHolder(binding.root)
    }
    val diffCallback = object : DiffUtil.ItemCallback<FetchProduct>(){
        override fun areItemsTheSame(oldItem: FetchProduct, newItem: FetchProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FetchProduct, newItem: FetchProduct): Boolean {
            return oldItem == newItem
        }

    }

    val differ1  = AsyncListDiffer(this, diffCallback)

    override fun getItemCount(): Int {
        return differ1.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductViewHolder, position: Int) {
        val category =  differ1.currentList[position]
        // Bind data to the ViewHolder
        holder.bind(category)


    }
}