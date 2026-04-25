package com.example.bcashoppingapplication.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.databinding.ActivityHomeBinding
import com.example.bcashoppingapplication.fragment.HomeFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore


class HomeActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the custom toolbar as the support action bar
        val toolbar = findViewById<Toolbar>(R.id.toolbar_custom)
        setSupportActionBar(toolbar)

        auth = Firebase.auth
        firebaseFirestore= Firebase.firestore

        homeFragment = HomeFragment()
        loadFragment(homeFragment)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_home,fragment)
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_go_to_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
                true
            }
            R.id.action_log_out -> {
                auth.signOut()
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}