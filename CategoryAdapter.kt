package com.example.bcashoppingapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.activities.AllProductsActivity
import com.example.bcashoppingapplication.model.Category
import com.example.bcashoppingapplication.util.UtilObject

class CategoryAdapter(var context: Context,private var categoryList: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {

        val view:View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_category_row_item,
                parent,false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder( holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]

        val drawableRes = context.resources.getIdentifier(category.drawableName,
            "drawable",context.packageName)
        holder.CategoryImage.setImageResource(drawableRes)

        holder.CategoryImage.setOnClickListener {

            UtilObject.screenNavigationWithDataPassing(context, AllProductsActivity::class.java,
                "categoryType",category.categoryType)
        }

    }

    override fun getItemCount(): Int {

        return categoryList.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val CategoryImage : ImageView = itemView.findViewById(R.id.iv_category)

    }

}