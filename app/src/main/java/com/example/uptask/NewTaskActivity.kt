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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NewTaskActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageButton
    private lateinit var etTextTaskName: EditText
    private lateinit var etTextTaskDate: EditText
    private lateinit var etTextTaskTime: EditText
    private lateinit var btnCreateTask: Button
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth
    
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_new_task)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        btnBack = findViewById(R.id.btnBackNewTask)
        etTextTaskName = findViewById(R.id.etTextTaskName)
        etTextTaskDate = findViewById(R.id.etTextTaskDate)
        etTextTaskTime = findViewById(R.id.etTextTaskTime)
        btnCreateTask = findViewById(R.id.btnCreateTask)
        
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.getReference("tasks")
        firebaseAuth = Firebase.auth
        
        btnBack.setOnClickListener { 
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        btnCreateTask.setOnClickListener { 
            val taskName = etTextTaskName.text.toString()
            val taskDate = etTextTaskDate.text.toString()
            val taskTime = etTextTaskTime.text.toString()
            
            if (taskName.isEmpty() && taskDate.isEmpty() && taskTime.isEmpty()) {
                etTextTaskName.error = "Task name is required"
                etTextTaskDate.error = "Task date is required"
                etTextTaskTime.error = "Task time is required"
            }
            if (taskName.isEmpty()) etTextTaskName.error = "Task name is required"
            if (taskDate.isEmpty()) etTextTaskDate.error = "Task date is required"
            if (taskTime.isEmpty()) etTextTaskTime.error = "Task time is required"
            
            if (taskName.isNotEmpty() && taskDate.isNotEmpty() && taskTime.isNotEmpty()) {
                saveTask(taskName, taskDate, taskTime)
            }
        }
    }

    private fun saveTask(taskName: String, taskDate: String, taskTime: String) {
        val id = System.currentTimeMillis().toString()
        val uId = firebaseAuth.currentUser?.uid.toString()
        val task = Task(id, taskName, taskDate, taskTime, "0", uId)
        databaseReference.child(id).setValue(task)
            .addOnCompleteListener(this) {t ->
                if (t.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Task created",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(
                        applicationContext,
                        t.exception?.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

    }
}