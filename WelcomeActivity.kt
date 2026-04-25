package com.example.bcashoppingapplication.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    lateinit var signupbtn: Button
    lateinit var loginbtn: Button
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth= FirebaseAuth.getInstance()
        setContentView(R.layout.activity_welcome)

        signupbtn=findViewById(R.id.sign_up_button)
        loginbtn=findViewById(R.id.log_in_btn)

        signupbtn.setOnClickListener {

            val intent= Intent(this@WelcomeActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
        loginbtn.setOnClickListener {

            val intent= Intent(this@WelcomeActivity, LogInActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser !=null){
            UtilObject.screenNavigation(this@WelcomeActivity, HomeActivity::class.java)
        }
    }

}

