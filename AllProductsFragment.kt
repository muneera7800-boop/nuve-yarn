package com.example.bcashoppingapplication

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bcashoppingapplication.adapter.BestsellerAdapter
import com.example.bcashoppingapplication.databinding.FragmentAllProductsBinding
import com.example.bcashoppingapplication.model.Crochet
import com.google.firebase.firestore.FirebaseFirestore

class AllProductsFragment : Fragment() {

    private var _binding: FragmentAllProductsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: BestsellerAdapter
    private var productList = mutableListOf<Crochet>()

    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllProductsBinding.inflate(inflater, container, false)
        firestore = FirebaseFirestore.getInstance()

        initRecyclerView()
        loadProducts()

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = BestsellerAdapter(requireContext(), productList) { product ->
            Toast.makeText(requireContext(), "Clicked: $", Toast.LENGTH_SHORT).show()
        }

        binding.rvAllProducts.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAllProducts.adapter = adapter
        addHorizontalSpacing(binding.rvAllProducts, 16)
    }

    private fun loadProducts() {
        firestore.collection("Bestseller").get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result!!) {
                        val product = doc.toObject(Crochet::class.java)
                        productList.add(product)
                        adapter.notifyItemInserted(productList.size - 1)
                    }
                }
            }
    }

    private fun addHorizontalSpacing(recyclerView: RecyclerView, space: Int) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                outRect.right = space
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}