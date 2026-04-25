package com.example.bcashoppingapplication.activities


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bcashoppingapplication.databinding.ActivityOrderPlacedBinding

class OrderPlacedActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderPlacedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderPlacedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the click listener for the button
        binding.btnContinueShopping.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)

            // This clears the backstack so the user goes back to a clean Home screen
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}