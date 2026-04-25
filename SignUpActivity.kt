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
import com.example.bcashoppingapplication.databinding.ActivityLogInBinding
import com.example.bcashoppingapplication.databinding.ActivitySignUpBinding
import com.example.bcashoppingapplication.util.UtilObject
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = Firebase.auth

        navigationOfViews()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun navigationOfViews() {

        binding.btnSignUp.setOnClickListener {
            signUpValidation()
        }
    }


    private fun SignUpActivity.signUpValidation() {

        val userName = binding.etNameSignup.text.toString().trim()
        val userEmail = binding.etEmailSignup.text.toString().trim()
        val userPassword = binding.etPasswordSignup.text.toString().trim()

        if (userName.isEmpty()&& userEmail.isEmpty() && userPassword.isEmpty()) {

            Toast.makeText(this,
                "Please Fill All The Details",
                Toast.LENGTH_SHORT).show()

        } else if (userName.isEmpty() ) {
            Toast.makeText(this,
                "Please Enter The Name",
                Toast.LENGTH_SHORT).show()

        } else if ( userEmail.isEmpty()) {
            Toast.makeText(
                this,
                "Please Enter The Email",
                Toast.LENGTH_SHORT).show()

        } else if ( userPassword.isEmpty()) {
            Toast.makeText(
                this,
                "Please Enter The Password",
                Toast.LENGTH_SHORT).show()

        }else if (!Patterns.EMAIL_ADDRESS.matcher( userEmail).matches()){
            Toast.makeText(this,
                "Enter Valid Email",
                Toast.LENGTH_SHORT).show()

        } else if (userPassword.length<6){

            Toast.makeText(this,
                " Please Enter Atleast 6 Characters",
                Toast.LENGTH_SHORT).show()

        } else { auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@SignUpActivity, HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                } else {
                    Toast.makeText(
                        baseContext,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,).show()
                }
            }

        }

    }
}
