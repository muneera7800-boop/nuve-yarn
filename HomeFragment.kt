package com.example.bcashoppingapplication

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcashoppingapplication.adapter.BestsellerAdapter
import com.example.bcashoppingapplication.adapter.CategoryAdapter
import com.example.bcashoppingapplication.adapter.FeaturedAdapter
import com.example.bcashoppingapplication.databinding.FragmentHomeBinding
import com.example.bcashoppingapplication.model.Category
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.activities.AllProductsActivity
import com.google.firebase.firestore.FirebaseFirestore

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseFirestore: FirebaseFirestore

    private var categoryList = mutableListOf<Category>()
    private var featuredList = mutableListOf<Crochet>()
    private var bestsellerList = mutableListOf<Crochet>()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var featuredAdapter: FeaturedAdapter
    private lateinit var bestsellerAdapter: BestsellerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        firebaseFirestore = FirebaseFirestore.getInstance()

        setupRecyclerViews()
        fetchData()
        setupSeeAllButtons()
        setupSearchBar()

        return binding.root
    }

    private fun setupSearchBar() {
        // ... inside setupSearchBar()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    val intent = Intent(requireContext(), AllProductsActivity::class.java)
                    intent.putExtra("searchQuery", query) // Sending the search term
                    startActivity(intent)
                }
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean { return true }
        })
    }

    private fun setupRecyclerViews() {
        categoryAdapter = CategoryAdapter(requireContext(), categoryList)
        binding.rvCategory.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategory.adapter = categoryAdapter
        addHorizontalSpacing(binding.rvCategory, 16)

        featuredAdapter = FeaturedAdapter(requireContext(), featuredList)
        binding.rvFeatured.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeatured.adapter = featuredAdapter
        addHorizontalSpacing(binding.rvFeatured, 16)

        bestsellerAdapter = BestsellerAdapter(requireContext(), bestsellerList) { product ->
            Toast.makeText(requireContext(), "Clicked: ${product.crochetName}", Toast.LENGTH_SHORT).show()
        }
        binding.rvBestseller.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBestseller.adapter = bestsellerAdapter
        addHorizontalSpacing(binding.rvBestseller, 16)
    }

    private fun fetchData() {
        // Category
        firebaseFirestore.collection("Category").get().addOnSuccessListener { snapshot ->
            categoryList.clear()
            for (doc in snapshot) {
                categoryList.add(doc.toObject(Category::class.java))
            }
            categoryAdapter.notifyDataSetChanged()
        }

        // Featured
        firebaseFirestore.collection("Featured").get().addOnSuccessListener { snapshot ->
            featuredList.clear()
            for (doc in snapshot) {
                featuredList.add(doc.toObject(Crochet::class.java))
            }
            featuredAdapter.notifyDataSetChanged()
        }

        // Bestseller
        firebaseFirestore.collection("Bestseller").get().addOnSuccessListener { snapshot ->
            bestsellerList.clear()
            for (doc in snapshot) {
                bestsellerList.add(doc.toObject(Crochet::class.java))
            }
            bestsellerAdapter.notifyDataSetChanged()
        }
    }

    private fun setupSeeAllButtons() {
        // Category See All - Passing "All" to show everything
        binding.textView8.setOnClickListener {
            startAllProductsActivity("All")
        }
        // Featured See All
        binding.textView10.setOnClickListener {
            startAllProductsActivity("Featured")
        }
        // Bestseller See All
        binding.textView12.setOnClickListener {
            startAllProductsActivity("Bestseller")
        }
    }

    private fun startAllProductsActivity(categoryType: String) {
        val intent = Intent(requireContext(), AllProductsActivity::class.java)
        intent.putExtra("categoryType", categoryType)
        startActivity(intent)
    }

    private fun addHorizontalSpacing(recyclerView: androidx.recyclerview.widget.RecyclerView, space: Int) {
        recyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.right = space
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}