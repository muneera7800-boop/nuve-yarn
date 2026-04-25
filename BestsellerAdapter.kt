package com.example.bcashoppingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.activities.ProductDetailActivity
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.util.UtilObject

class BestsellerAdapter(
    private val context: Context,
    private val productList: MutableList<Crochet>,
    private val onItemClick: (Crochet) -> Unit
) : RecyclerView.Adapter<BestsellerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bestsellerfl: FrameLayout = itemView.findViewById(R.id.fl_bestseller_row_item)
        val bestsellerImage: ImageView = itemView.findViewById(R.id.iv_bestseller)
        val bestsellerCrochetPrice: TextView = itemView.findViewById(R.id.tv_bestseller_crochet_price)
        val bestsellerCrochetName: TextView = itemView.findViewById(R.id.tv_bestseller_crochet_name)

        fun bind(product: Crochet) {
            bestsellerCrochetName.text = product.crochetName
            
            val strPrice = product.crochetPrice.toString()
            val priceWithSymbol = context.getString(R.string.rupee_sign, strPrice)
            bestsellerCrochetPrice.text = priceWithSymbol

            // Load image from drawable
            val drawableRes = context.resources.getIdentifier(
                product.crochetDrawableName, "drawable", context.packageName
            )
            bestsellerImage.setImageResource(
                if (drawableRes != 0) drawableRes else R.drawable.ic_launcher_background
            )

            // Click listener
            bestsellerfl.setOnClickListener {
                onItemClick(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rv_bestseller_row_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int = productList.size
}