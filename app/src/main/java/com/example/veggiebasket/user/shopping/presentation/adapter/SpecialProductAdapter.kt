package com.example.veggiebasket.user.shopping.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.SpecialRvItemBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

class SpecialProductAdapter: RecyclerView.Adapter<SpecialProductAdapter.myViewHolder>() {
    inner class myViewHolder(private val binding: SpecialRvItemBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(product: FetchProduct){
            binding.apply {
                val firstImageUrl = product.imageUrl.entries.firstOrNull()?.value
                if (!firstImageUrl.isNullOrEmpty()) {
                    Glide.with(itemView).load(firstImageUrl).into(imageSpecialRvItem)
                } else {
                    // Load a placeholder or handle the case where imageUrl is empty
                }
                tvSpecialProductName.text = product.name
                tvSpecialPrdouctPrice.text = product.price.toString()
            }
        }
    }

    val diffCallback = object : DiffUtil.ItemCallback<FetchProduct>(){
        override fun areItemsTheSame(oldItem: FetchProduct, newItem: FetchProduct): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FetchProduct, newItem: FetchProduct): Boolean {
           return oldItem == newItem
        }

    }

    val differ  = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        return myViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
       return differ.currentList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }

}