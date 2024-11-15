package com.example.uptask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignInActivity : AppCompatActivity() {
    private lateinit var etTextEmail: EditText
    private lateinit var etTextPassword: EditText
    private lateinit var tvForgotPass: TextView
    private lateinit var btnSignIn: Button
    private lateinit var btnSignInWithGoogle: CardView
    private lateinit var tvSignUp: TextView
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

        tvSignUp.setOnClickListener {
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}