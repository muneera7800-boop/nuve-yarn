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
import com.example.bcashoppingapplication.adapter.PaymentProductAdapter
import com.example.bcashoppingapplication.databinding.ActivityPaymentBinding
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.model.SelectedAddress
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivity : AppCompatActivity() {

    private lateinit var allProductList: List<Crochet>
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        UtilObject.setUpCustomToolBar(this@PaymentActivity)

        setupRecyclerView()
        getDataFromSelectedAddress()
        calculateTotalAndDisplayProducts()
        navigationOfViews()
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRecyclerView() {
        binding.rvPaymentProducts.layoutManager = LinearLayoutManager(this)
    }

    private fun navigationOfViews() {
        binding.btnPayNow.setOnClickListener {
            clearCartAndPlaceOrder()
        }
    }

    private fun clearCartAndPlaceOrder() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val cartRef = firestore.collection("Users").document(userId).collection("Cart")
            
            cartRef.get().addOnSuccessListener { snapshot ->
                val batch = firestore.batch()
                for (doc in snapshot) {
                    batch.delete(doc.reference)
                }
                
                batch.commit().addOnSuccessListener {
                    val intent = Intent(this, OrderPlacedActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to process order", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error accessing cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateTotalAndDisplayProducts() {
        allProductList = intent.getSerializableExtra("fireStoreProductList") as? List<Crochet> ?: emptyList()

        // Display products in list
        val adapter = PaymentProductAdapter(allProductList)
        binding.rvPaymentProducts.adapter = adapter

        // Calculate total
        var totalAmount = 0.0
        for (product in allProductList) {
            val productQty = product.crochetQuantity ?: 0
            val productPrice = product.crochetPrice.toDouble()
            totalAmount += productQty * productPrice
        }

        val totalAmountWithRupeeSign = getString(R.string.rupee_sign, totalAmount.toString())
        binding.tvPaymentTotalValue.text = totalAmountWithRupeeSign
    }

    private fun getDataFromSelectedAddress() {
        val selectAddress = intent.getParcelableExtra<SelectedAddress>("selectedAddress")
        binding.tvDeliveryAddress.text = selectAddress?.address ?: "No Address Selected"
    }
}