package com.example.myapplication

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var coursesListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dbHelper = DatabaseHelper(this)
        coursesListView = findViewById(R.id.courses_list)

        loadCourses()
    }

    private fun loadCourses() {
        val courses = dbHelper.getAllCourses()
        if (courses.isNotEmpty()) {
            // Предполагается, что у вас есть адаптер для отображения курсов в ListView
            val adapter = CourseAdapter(this, courses)
            coursesListView.adapter = adapter
        } else {
            Toast.makeText(this, "Нет доступных курсов", Toast.LENGTH_LONG).show()
        }
    }
}
