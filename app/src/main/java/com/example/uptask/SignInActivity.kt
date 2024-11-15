package com.example.uptask

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth

class SignInActivity : AppCompatActivity() {
    private lateinit var etTextEmail: EditText
    private lateinit var etTextPassword: EditText
    private lateinit var tvForgotPass: TextView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignInWithGoogle: CardView
    private lateinit var tvSignUp: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var emailInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etTextEmail = findViewById(R.id.etTextEmailAddressSignIn)
        etTextPassword = findViewById(R.id.etTextPasswordSignIn)
        tvForgotPass = findViewById(R.id.tvForgotPassword)
        btnSignIn = findViewById(R.id.btnSignInSignIn)
        btnSignInWithGoogle = findViewById(R.id.btnSignInWithGoogle)
        tvSignUp = findViewById(R.id.tvSignUpSignIn)
        progressBar = findViewById(R.id.progressBarSignIn)

        emailInput = findViewById(R.id.textInputLayout)
        passwordInput = findViewById(R.id.textInputLayout2)

        firebaseAuth = Firebase.auth

        tvSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvForgotPass.setOnClickListener {
            val intent = Intent(applicationContext, ForgotPassActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignIn.setOnClickListener {
            val email = etTextEmail.text.toString()
            val password = etTextPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                emailInput.error = "Email is required"
                passwordInput.error = "Password is required"
            }
            if (email.isEmpty()) emailInput.error = "Email is required"
            if (password.isEmpty()) passwordInput.error = "Password is required"

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.VISIBLE
                    val intent = Intent(applicationContext, MainActivity::class.java)
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

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}