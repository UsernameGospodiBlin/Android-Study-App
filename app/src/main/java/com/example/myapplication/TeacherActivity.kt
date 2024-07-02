package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class TeacherActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var tasksListView: ListView
    private lateinit var feedbackEditText: EditText
    private lateinit var sendFeedbackButton: Button
    private lateinit var sendForReviewButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        dbHelper = DatabaseHelper(this)
        tasksListView = findViewById(R.id.tasks_list)
        feedbackEditText = findViewById(R.id.feedback_text)
        sendFeedbackButton = findViewById(R.id.button_send_feedback)
        sendForReviewButton = findViewById(R.id.button_send_for_review)

        loadTasks()

        sendFeedbackButton.setOnClickListener {
            sendFeedback()
        }

        sendForReviewButton.setOnClickListener {
            sendForReview()
        }
    }

    private fun loadTasks() {
        val tasks = dbHelper.getAllTasks()
        if (tasks.isNotEmpty()) {
            val adapter = TaskAdapter(this, tasks)
            tasksListView.adapter = adapter
        }
    }

    private fun sendFeedback() {
        val feedback = feedbackEditText.text.toString().trim()
        if (feedback.isNotEmpty()) {
            // код для отправки отзыва
            Toast.makeText(this, "Отзыв отправлен", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Введите отзыв", Toast.LENGTH_LONG).show()
        }
    }

    private fun sendForReview() {
        // код для отправки на дополнительную проверку
        Toast.makeText(this, "Задание отправлено на дополнительную проверку", Toast.LENGTH_LONG).show()
    }
}
