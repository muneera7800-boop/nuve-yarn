package com.example.bcashoppingapplication.activities


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.activities.WelcomeActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    companion object {
        const val TIME_LIMIT = 2000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {

            delay(TIME_LIMIT)
            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)

            startActivity(intent)
            finish()
        }
    }
}


