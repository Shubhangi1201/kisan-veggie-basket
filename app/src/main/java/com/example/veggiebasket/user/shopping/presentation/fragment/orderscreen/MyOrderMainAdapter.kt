package com.example.veggiebasket.user.shopping.presentation.fragment.orderscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.SingleOrderLayoutBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.MyOrderData

class MyOrderMainAdapter(
    val list: ArrayList<MyOrderData>,
    private val orderAgain: (MyOrderData) -> Unit

    ): RecyclerView.Adapter<MyOrderMainAdapter.myViewHolder>()  {

    inner class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = SingleOrderLayoutBinding.bind(itemView)
        fun bind(product: MyOrderData){
            binding.apply {
               date.text = "Ordered on ${product.date}"
                name.text = product.name
                price.text = "Price : ${product.price}"
                quantity.text = "Qty: ${product.qty}"
                totalAmount.text = "₹${product.qty.toInt() * product.price.toInt()}"
                soldby.text = "sold by: ${product.sellerName}"
                buyAgainBtn.setOnClickListener {
                    orderAgain(product)
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = SingleOrderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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