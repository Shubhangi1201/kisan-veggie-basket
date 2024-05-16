package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.SingleCategoryLayoutBinding
import com.example.veggiebasket.databinding.SpecialRvItemBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

class SingleCategoryAdapter(
            val list: List<FetchProduct>,
            private val navigateToInfoFragment: (FetchProduct) -> Unit
):
    RecyclerView.Adapter<SingleCategoryAdapter.myViewHolder>(){


    inner class myViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SingleCategoryLayoutBinding.bind(itemView)

        fun bind(fetchProduct: FetchProduct) {
            binding.categoryName.text = fetchProduct.name

            val imageUrlMap = fetchProduct.imageUrl

            // Load the first image URL if it exists
            val firstImageUrl = imageUrlMap.values.firstOrNull()
            if (firstImageUrl != null) {
                Glide.with(itemView)
                    .load(firstImageUrl)
                    .into(binding.categoryImage)
            } else {
                // Set a placeholder or handle the case where no image URL is available
                // Glide.with(itemView).load(R.drawable.placeholder).into(binding.imageSpecialRvItem)
            }

            itemView.setOnClickListener{
                navigateToInfoFragment(fetchProduct)
            }


        }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): myViewHolder {
        val binding = SingleCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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