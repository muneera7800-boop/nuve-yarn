package com.example.bcashoppingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.databinding.RvProductSearchItemRowBinding
import com.example.bcashoppingapplication.model.Crochet

class AllProductsAdapter(
    private val context: Context,
    private val allProductList: MutableList<Crochet>,
    private val onItemClick: (Crochet) -> Unit
) : RecyclerView.Adapter<AllProductsAdapter.AllProductsViewHolder>() {

    inner class AllProductsViewHolder(val binding: RvProductSearchItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Crochet) {
            // Name
            binding.tvProductName.text = product.crochetName

            // Price
            val price = product.crochetPrice ?: 0.0
            binding.tvProductPrice.text = context.getString(R.string.rupee_sign, price)

            // Image
            val drawableRes = context.resources.getIdentifier(
                product.crochetDrawableName,
                "drawable",
                context.packageName
            )
            binding.ivProductImage.setImageResource(
                if (drawableRes != 0) drawableRes else R.drawable.ic_launcher_background
            )

            // Click listener
            binding.flAllProductItemRow.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllProductsViewHolder {
        val binding = RvProductSearchItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AllProductsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AllProductsViewHolder, position: Int) {
        holder.bind(allProductList[position])
    }

    override fun getItemCount(): Int = allProductList.size
}