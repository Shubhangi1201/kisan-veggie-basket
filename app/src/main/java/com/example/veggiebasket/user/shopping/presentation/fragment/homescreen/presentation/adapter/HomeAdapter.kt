package com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.SingleCategoryLayoutBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.CategoryData

class HomeAdapter(
    private val navigateToNextFragment: (String) -> Unit
) :
    RecyclerView.Adapter<HomeAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = SingleCategoryLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding.root)
    }
    val diffCallback = object : DiffUtil.ItemCallback<CategoryData>(){
        override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
            return oldItem == newItem
        }

    }

    val differ  = AsyncListDiffer(this, diffCallback)

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category =  differ.currentList[position]
        // Bind data to the ViewHolder
        holder.bind(category)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // ViewHolder class
    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = SingleCategoryLayoutBinding.bind(itemView)

        fun bind(category: CategoryData) {
            // Bind data to the UI elements in the ViewHolder
            binding.categoryName.text = category.name
            Glide.with(itemView)
                .load(category.url)
                .into(binding.categoryImage)
            // Add more bindings as needed

            itemView.setOnClickListener {
                navigateToNextFragment(category.name!!)
            }
        }
    }
}
