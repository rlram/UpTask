package com.example.uptask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPassActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextEmail: EditText
    private lateinit var btnSubmit: Button
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var emailInput: TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_forgot_pass)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackForgotPass)
        etTextEmail = findViewById(R.id.etTextEmailAddressForgotPass)
        btnSubmit = findViewById(R.id.btnSubmitForgotPass)

        emailInput = findViewById(R.id.textInputLayout)

        firebaseAuth = Firebase.auth

        btnBack.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSubmit.setOnClickListener {
            val email = etTextEmail.text.toString()

            if (email.isEmpty()) emailInput.error = "Email is required"
            if (email.isNotEmpty()) {
                sendResetLink(email)
            }
        }
    }

    private fun sendResetLink(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Check your mail",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(applicationContext, SignInActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
}