package com.example.bcashoppingapplication.activities

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.databinding.ActivityAddAddressBinding
import com.example.bcashoppingapplication.model.SelectedAddress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddAddressBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Logic for Confirm Button
        binding.btnConfirmAddress.setOnClickListener {
            val name = binding.etAddName.text.toString()
            val address = binding.etAddFullAddress.text.toString()
            val city = binding.etAddCity.text.toString()
            val postalCode = binding.etAddPostalCode.text.toString()
            val phone = binding.etAddPhoneNumber.text.toString()

            // Validate that no fields are empty
            if (name.isNotEmpty() && address.isNotEmpty() && city.isNotEmpty() &&
                postalCode.isNotEmpty() && phone.isNotEmpty()) {

                // Combine into a single address string for your Model
                val fullAddressString = "$name, $address, $city, $postalCode, Ph: $phone"
                saveAddressToFirestore(fullAddressString)

            } else {
                Toast.makeText(this, "Please fill in all details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveAddressToFirestore(fullAddress: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            // Create the address object
            val addressObject = SelectedAddress(address = fullAddress, isSelected = false)

            // Save to: Users -> [UID] -> Address -> [AutoID]
            firestore.collection("Users")
                .document(userId)
                .collection("Address")
                .add(addressObject)
                .addOnSuccessListener {
                    Toast.makeText(this, "Address Saved Successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close this screen and go back
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
}