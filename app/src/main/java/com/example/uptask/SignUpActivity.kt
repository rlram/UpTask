package com.example.uptask

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextName: EditText
    private lateinit var etTextEmail: EditText
    private lateinit var etTextPassword: EditText
    private lateinit var etTextConfirmPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private lateinit var nameInput: TextInputLayout
    private lateinit var emailInput: TextInputLayout
    private lateinit var passwordInput: TextInputLayout
    private lateinit var confirmPasswordInput: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackSignUp)
        etTextName = findViewById(R.id.etTextNameSignUp)
        etTextEmail = findViewById(R.id.etTextEmailAddressSignUp)
        etTextPassword = findViewById(R.id.etTextPasswordSignUp)
        etTextConfirmPassword = findViewById(R.id.etTextConfirmPasswordSignUp)
        btnSignUp = findViewById(R.id.btnSignUpSignUp)
        progressBar = findViewById(R.id.progressBarSignUp)

        nameInput = findViewById(R.id.textInputLayout)
        emailInput = findViewById(R.id.textInputLayout2)
        passwordInput = findViewById(R.id.textInputLayout3)
        confirmPasswordInput = findViewById(R.id.textInputLayout4)

        firebaseAuth = Firebase.auth

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("users")

        btnBack.setOnClickListener {
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSignUp.setOnClickListener {
            val name = etTextName.text.toString()
            val email = etTextEmail.text.toString()
            val password = etTextPassword.text.toString()
            val confirmPassword = etTextConfirmPassword.text.toString()

            if (name.isEmpty() && email.isEmpty() && password.isEmpty() && confirmPassword.isEmpty()) {
                nameInput.error = "Name is required"
                emailInput.error = "Email is required"
                passwordInput.error = "Password is required"
                confirmPasswordInput.error = "Confirm password is required"
            }
            if (name.isEmpty()) nameInput.error = "Name is required"
            if (email.isEmpty()) emailInput.error = "Email is required"
            if (password.isEmpty()) passwordInput.error = "Password is required"
            if (confirmPassword.isEmpty()) confirmPasswordInput.error = "Confirm password is required"

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    signUpUser(name, email, password)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Password didn't match",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    private fun signUpUser(name: String, email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.VISIBLE
                    databaseReference.child(firebaseAuth.currentUser?.uid.toString()).child("name").setValue(name)
                    databaseReference.child(firebaseAuth.currentUser?.uid.toString()).child("email").setValue(email)
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
}