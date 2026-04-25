package com.example.bcashoppingapplication.util

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bcashoppingapplication.activities.CartActivity
import com.example.bcashoppingapplication.activities.ProductDetailActivity
import com.example.bcashoppingapplication.R
import com.google.android.material.snackbar.Snackbar
import java.io.Serializable

object UtilObject {

    fun screenNavigation(context: Context, destination: Class<*>) {
        val intent = Intent(context, destination)
        context.startActivity(intent)
    }

    fun screenNavigationWithDataPassing(
        context: Context,
        destination: Class<*>,
        key: String,
        value: Any
    ) {
        val intent = Intent(context, destination)

        when (value) {
            is String -> intent.putExtra(key, value)
            is Parcelable -> intent.putExtra(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }

        context.startActivity(intent)
    }


    fun screenNavigationWithDataPassingSerialize(
        context: Context, destination: Class<*>,
        key: String? , value: Serializable?){

        val intent = Intent(context, destination)
        intent.putExtra(key, value )
        context.startActivity(intent)
}

    fun setUpCustomToolBar(activity: AppCompatActivity) {
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar_custom)
        activity.setSupportActionBar(toolbar)

        val actionBar = activity.supportActionBar
        if (actionBar != null) {
            actionBar.title = ""
            actionBar.setDisplayHomeAsUpEnabled(true)
        }




        toolbar?.setNavigationOnClickListener {
            activity.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun snackBar(view: View, text: String) {
        Snackbar.make(view,text, Snackbar.LENGTH_LONG).show()
    }

}