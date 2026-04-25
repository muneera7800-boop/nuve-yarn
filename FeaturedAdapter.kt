package com.example.bcashoppingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.activities.ProductDetailActivity
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.util.UtilObject


class FeaturedAdapter(var context: Context,
   private var  featuredList: MutableList<Crochet>)
    : RecyclerView.Adapter<FeaturedAdapter.FeaturedViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int ): FeaturedViewHolder {
        val view : View = LayoutInflater.from(context)
            .inflate(R.layout.rv_featured_row_item,parent,false)

        return FeaturedViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: FeaturedViewHolder,
        position: Int,
    ) {
        val featured = featuredList[position]

        val drawableRes = context.resources.getIdentifier(
            featured.crochetDrawableName,
            "drawable",
            context.packageName
        )
        if (drawableRes != 0) {
            holder.featuredImage.setImageResource(drawableRes)
        } else {
            holder.featuredImage.setImageResource(R.drawable.ic_launcher_background)
        }
        val strPrice = featured.crochetPrice.toString()
        val priceWithSymbol = context.getString(R.string.rupee_sign, strPrice)
        holder.featuredCrochetPrice.text = priceWithSymbol
        holder.featuredCrochetName.text = featured.crochetName

        holder.featuredfl.setOnClickListener {

            UtilObject.screenNavigationWithDataPassing(context,
                ProductDetailActivity::class.java,"productDetails",featured)


        }

    }
    override fun getItemCount(): Int {
        return featuredList.size
    }

    class FeaturedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val featuredfl: FrameLayout= itemView.findViewById(R.id.fl_featured_row_item)
        val featuredImage : ImageView = itemView.findViewById(R.id.iv_featured)
        val featuredCrochetPrice : TextView = itemView.findViewById(R.id.tv_featured_crochet_price)
        val featuredCrochetName : TextView = itemView.findViewById(R.id.tv_featured_crochet_name)

    }
}