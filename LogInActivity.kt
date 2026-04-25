package com.example.bcashoppingapplication.activities

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bcashoppingapplication.R
import com.example.bcashoppingapplication.activities.HomeActivity
import com.example.bcashoppingapplication.databinding.ActivityLogInBinding
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LogInActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLogInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        auth = Firebase.auth
        navigationOfViews()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigationOfViews() {

        binding.btnLogin.setOnClickListener {
            loginValidation()
        }
    }
    private fun loginValidation() {
        val userEmail = binding.etEmailLogin.text.toString().trim()
        val userPassword = binding.etPasswordLogin.text.toString().trim()

        if (userEmail.isEmpty() && userPassword.isEmpty()) {

            Toast.makeText(
                this,
                "please fill all the details",
                Toast.LENGTH_SHORT).show()

        } else if (userEmail.isEmpty()) {

            Toast.makeText(this,
                "please fill the email id",
                Toast.LENGTH_SHORT).show()

        }else if (userPassword.isEmpty()){

            Toast.makeText(this,
                "plase fill the password",
                Toast.LENGTH_SHORT).show()

        } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){

            Toast.makeText(this,
                "please enter valid email",
                Toast.LENGTH_SHORT).show()
        } else if (userPassword.length<6){

            Toast.makeText(this,
                "please enter atleast 6 character",
                Toast.LENGTH_SHORT).show()

        }else {
            auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this,
                            "logged In",
                            Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {

                        Toast.makeText( this,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }
        }



    }
}

