package com.example.uptask

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var ivUserProfile: ImageView
    private lateinit var tvUserName: TextView
    private lateinit var btnCalendar: ImageButton
    private lateinit var btnNotification: ImageButton
    private lateinit var tvDateDay: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var taskCompletionPercent: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvNoTasks: TextView
    private lateinit var btnAddNewTask: ImageButton
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReferenceUser: DatabaseReference
    private lateinit var databaseReferenceTask: DatabaseReference
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: TaskAdapter
    private var taskCompletionCount = 0
    private val taskList = ArrayList<Task>()

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ivUserProfile = findViewById(R.id.ivUserProfile)
        tvUserName = findViewById(R.id.tvUserName)
        btnCalendar = findViewById(R.id.btnCalendar)
        btnNotification = findViewById(R.id.btnNotification)
        tvDateDay = findViewById(R.id.tvDateDay)
        tvTaskCount = findViewById(R.id.taskCount)
        taskCompletionPercent = findViewById(R.id.taskCompletionPercent)
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBarMain)
        tvNoTasks = findViewById(R.id.tvNoTasks)
        btnAddNewTask = findViewById(R.id.btnAddNewTask)

        firebaseAuth = Firebase.auth
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReferenceUser = firebaseDatabase.getReference("users")
        databaseReferenceTask = firebaseDatabase.getReference("tasks")

        val uId = firebaseAuth.currentUser?.uid.toString()

        databaseReferenceUser.child(uId).child("name").get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val name = it.value.toString()
                    val firstName = name.split(" ")
                    tvUserName.text = firstName[0]
                }
            }

        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val formattedDate = currentDate.format(formatter)

        val currentDateDay = "${formattedDate}, ${currentDate.dayOfWeek}"
        tvDateDay.text = currentDateDay

        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = TaskAdapter(taskList)
        recyclerView.adapter = adapter

        databaseReferenceTask.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    progressBar.visibility = View.GONE

                    for (item in snapshot.children) {
                        if (item.child("userId").value.toString() == uId) {
                            val id = item.child("id").value.toString()
                            val taskName = item.child("taskName").value.toString()
                            val taskDate = item.child("taskDate").value.toString()
                            val taskTime = item.child("taskTime").value.toString()
                            val isCompleted = item.child("isCompleted").value.toString()
                            val usrId = item.child("uId").value.toString()

                            taskList.add(Task(id, taskName, taskDate, taskTime, isCompleted, usrId))
                            adapter.notifyItemInserted(taskList.size)
                            tvTaskCount.text = "$taskCompletionCount / ${taskList.size} Tasks"
                            if (taskList.size != 0) {
                                val percent = (taskCompletionCount / taskList.size) * 100;
                                taskCompletionPercent.text = "$percent%"
                            }
                        } else {
                            tvNoTasks.visibility = View.VISIBLE
                        }
                    }
                } else {
                    progressBar.visibility = View.GONE
                    tvNoTasks.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    error.message,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

        btnAddNewTask.setOnClickListener {
            val intent = Intent(applicationContext, NewTaskActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}