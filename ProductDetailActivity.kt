package com.example.bcashoppingapplication.activities

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.databinding.ActivityProductDetailBinding
import com.example.bcashoppingapplication.model.Crochet
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

class ProductDetailActivity : AppCompatActivity() {

    var product : Crochet? = null

    lateinit var firestore: FirebaseFirestore
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var binding: ActivityProductDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth
        firestore= Firebase.firestore

        UtilObject.setUpCustomToolBar(this)

        getDataFromIntent()
        getProductQty()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getProductQty() {

        binding.btnIncreaseProductQty.setOnClickListener {

            val currentValue = binding.btnProductQuantity.text.toString()
            var qtyValue = currentValue.toInt()
            qtyValue++
            binding.btnProductQuantity.text=qtyValue.toString()
        }
        binding.btnDecreaseProductQty.setOnClickListener {

            val currentValue = binding.btnProductQuantity.text.toString()
            var qtyValue = currentValue.toInt()
            if (qtyValue>1){
                qtyValue--
            }
            binding.btnProductQuantity.text=qtyValue.toString()
        }

    }
    private fun getDataFromIntent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            product = intent.getParcelableExtra("productDetails", Crochet::class.java)
        }else{
            product= intent.getParcelableExtra("productDetails")
        }
        product?.let {
            getDataFromProduct(it)
            checkRatingAndSetDescription(it)
            navigationOfViews(it)
        }
    }

    private fun navigationOfViews(product: Crochet) {
        binding.btnAddToCart.setOnClickListener {

            Log.d("IMAGE_CHECK", product.crochetDrawableName.toString())

            val productQty = binding.btnProductQuantity.text.toString().toInt()

            val cartItem = Crochet(
                crochetName = product.crochetName,
                crochetDescription = product.crochetDescription,
                crochetPrice = product.crochetPrice,
                crochetDrawableName = product.crochetDrawableName,
                categoryType = product.categoryType,
                crochetRating = product.crochetRating,
                crochetQuantity = productQty,
                documentId = product.documentId)

            addProductToCart(cartItem)

            UtilObject.screenNavigation(this@ProductDetailActivity,
                CartActivity::class.java)
            

        }
    }

    fun addProductToCart(cartItem: Crochet) {

        val userId = firebaseAuth.currentUser ?.uid

        if (userId != null) {
            firestore.collection(
                "Users"
            )
                .document(userId)
                .collection("Cart")
                .add(cartItem)
                .addOnCompleteListener {

                    Toast.makeText(
                        this@ProductDetailActivity,
                        "Item Added To Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                }.addOnFailureListener {

                    Toast.makeText(this@ProductDetailActivity,
                        "Failed To Add The Item",
                         Toast.LENGTH_SHORT
                    ).show()
                }
        }

    }


    fun checkRatingAndSetDescription(product: Crochet) {
        val rating= product.crochetRating
        val ratingDescription = when {
            rating>=4.5 -> "Excellent"
            rating>=4.0 -> "Good"
            else -> {"Average"}
        }
        binding.tvRatingDescription.text = ratingDescription
    }
    fun getDataFromProduct(product: Crochet) {
        val drawableResId = resources.getIdentifier(product.crochetDrawableName,
            "drawable",packageName)
        if (drawableResId != 0 ){
            binding.ivProductItem.setImageResource(drawableResId)
        }
      val priceWithSymbol = getString(R.string.rupee_sign,product.crochetPrice.toString())
         binding.tvProductPrice.text=priceWithSymbol
        binding.tvProductDescription.text = product.crochetDescription
        binding.tvProductName.text = product.crochetName
        binding.btnProductRating.text = product.crochetRating.toString()
    }


}