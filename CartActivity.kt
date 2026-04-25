package com.example.bcashoppingapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.adapter.CartItemAdapter
import com.example.bcashoppingapplication.databinding.ActivityCartBinding
import com.example.bcashoppingapplication.model.Crochet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.io.Serializable

class CartActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    private val cartItemList = mutableListOf<Crochet>()
    private lateinit var cartItemAdapter: CartItemAdapter
    lateinit var binding: ActivityCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        recyclerViewInit()
        navigationOfViews()
        getCartData()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigationOfViews() {
        binding.btnOrderNow.setOnClickListener {
            if (cartItemList.isNotEmpty()) {
                val intent = Intent(this, SelectAddressActivity::class.java)
                val productArrayList = ArrayList(cartItemList)
                intent.putExtra("fireStoreProductList", productArrayList)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun recyclerViewInit() {
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.setHasFixedSize(true)

        cartItemAdapter = CartItemAdapter(
            this,
            cartItemList,
            firestore,
            firebaseAuth,
            CartItemUpdateImpl(this@CartActivity)
        )
        binding.rvCart.adapter = cartItemAdapter
    }

    private fun calculateTotalAmountOfAllProduct() {
        var totalAmount = 0.0
        for (cartItem in cartItemList) {
            cartItem.crochetQuantity?.let { quantity ->
                cartItem.crochetPrice?.let { price ->
                    totalAmount += quantity.toDouble() * price
                }
            }
        }
        binding.tvTotalValue.text = "Overall Total Amount: ₹$totalAmount"
    }

    private fun getCartData() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection("Users")
            .document(userId)
            .collection("Cart")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    cartItemList.clear()
                    for (document in task.result.documents) {
                        val cartItem = document.toObject(Crochet::class.java)
                        cartItem?.let {
                            it.documentId = document.id
                            cartItemList.add(it)
                        }
                    }
                    calculateTotalAmountOfAllProduct()
                    cartItemAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "Failed to load cart data!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    class CartItemUpdateImpl(private val cartActivity: CartActivity) :
        CartItemAdapter.CartItemViewHolder.CartUpdateListener {
        override fun onCartUpdate(itemList: List<Crochet>) {
            cartActivity.calculateTotalAmountOfAllProduct()
        }
    }
}