package com.example.veggiebasket.admin.fragments.admin_myproduct_screen.domain

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.veggiebasket.databinding.AdminMyorderSinglerowBinding
import com.example.veggiebasket.user.shopping.presentation.fragment.cartscreen.CartAdapter
import com.example.veggiebasket.user.shopping.presentation.fragment.homescreen.data.FetchProduct

class AdminProductAdapter(
    val list: ArrayList<FetchProduct>,
    private val editProduct : (FetchProduct) -> Unit,
    private val removeProduct: (FetchProduct) -> Unit

): RecyclerView.Adapter<AdminProductAdapter.myViewHolder>()  {
    inner class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val binding = AdminMyorderSinglerowBinding.bind(itemView)
        fun bind(product: FetchProduct){

            binding.apply {
                msCSRname3.text = product.name
                msCSRprice3.text = "Price: ${product.price}"
                msCSRofferpercentage3.text = "Offer ${product.offerPercentage}%"
                msquantity3.text = "Weight : ${product.weight}"
                msEdit.setOnClickListener{
                    editProduct(product)
                }
                adminRemoveProduct.setOnClickListener{
                    removeProduct(product)
                }

            }

            val firstImageUrl = product.imageUrl.entries.firstOrNull()?.value
            if (!firstImageUrl.isNullOrEmpty()) {
                Glide.with(itemView).load(firstImageUrl).into(binding.msimageView23)
            } else {
                // Load a placeholder or handle the case where imageUrl is empty
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val binding = AdminMyorderSinglerowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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