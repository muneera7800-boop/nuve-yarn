package com.example.bcashoppingapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.databinding.RvPaymentItemRowBinding
import com.example.bcashoppingapplication.model.Crochet

class PaymentProductAdapter(private val productList: List<Crochet>) :
    RecyclerView.Adapter<PaymentProductAdapter.ViewHolder>() {

    class ViewHolder(val binding: RvPaymentItemRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvPaymentItemRowBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.binding.tvPaymentProductName.text = product.crochetName
        holder.binding.tvPaymentProductQty.text = "Qty: ${product.crochetQuantity}"
        holder.binding.tvPaymentProductPrice.text = "₹${product.crochetPrice}"
    }

    override fun getItemCount(): Int = productList.size
}