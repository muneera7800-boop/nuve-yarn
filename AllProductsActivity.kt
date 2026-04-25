package com.example.bcashoppingapplication.activities

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.bcashoppingapplication.adapter.AllProductsAdapter
import com.example.bcashoppingapplication.databinding.ActivityAllProductsBinding
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.firestore.FirebaseFirestore

class AllProductsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllProductsBinding
    private lateinit var allProductsAdapter: AllProductsAdapter
    private var allProductList = mutableListOf<Crochet>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        UtilObject.setUpCustomToolBar(this)

        // 1. Get Data from Intent
        val type = intent.getStringExtra("categoryType") ?: ""
        val searchQuery = intent.getStringExtra("searchQuery") ?: ""

        setupRecyclerView()

        // 2. Decide what to load
        if (searchQuery.isNotEmpty()) {
            performSearch(searchQuery)
        } else {
            loadCategoryProducts(type)
        }
    }

    private fun setupRecyclerView() {
        allProductsAdapter = AllProductsAdapter(this, allProductList) { product ->
            UtilObject.screenNavigationWithDataPassing(this, ProductDetailActivity::class.java, "productDetails", product)
        }
        binding.rvAllProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvAllProducts.adapter = allProductsAdapter
    }

    private fun performSearch(query: String) {
        // Search across ALL products in the "AllProducts" collection
        db.collection("AllProducts").get().addOnSuccessListener { snapshot ->
            allProductList.clear()
            val searchTerm = query.lowercase()
            for (doc in snapshot) {
                val product = doc.toObject(Crochet::class.java)
                if (product.crochetName.lowercase().contains(searchTerm)) {
                    product.documentId = doc.id
                    allProductList.add(product)
                }
            }
            allProductsAdapter.notifyDataSetChanged()
            if (allProductList.isEmpty()) Toast.makeText(this, "No items found for '$query'", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadCategoryProducts(type: String) {
        val collection = when (type) {
            "Featured" -> "Featured"
            "Bestseller" -> "Bestseller"
            else -> "AllProducts" // Categories (like Bags) are filtered from AllProducts
        }

        var query = db.collection(collection)

        // If it's a specific Category from the Home list (e.g. "Bags")
        if (collection == "AllProducts" && type != "All" && type.isNotEmpty()) {
            query.whereEqualTo("categoryType", type).get().addOnSuccessListener { processSnapshot(it) }
        } else {
            query.get().addOnSuccessListener { processSnapshot(it) }
        }
    }

    private fun processSnapshot(snapshot: com.google.firebase.firestore.QuerySnapshot) {
        allProductList.clear()
        for (doc in snapshot) {
            val product = doc.toObject(Crochet::class.java)
            product.documentId = doc.id
            allProductList.add(product)
        }
        allProductsAdapter.notifyDataSetChanged()
    }
}