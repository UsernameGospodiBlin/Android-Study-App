package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AdminActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var coursesListView: ListView
    private lateinit var coursesAdapter: ArrayAdapter<String>
    private lateinit var usersListView: ListView
    private lateinit var usersAdapter: UserAdapter
    private var courses: List<Course> = emptyList()
    private var users: List<User> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        dbHelper = DatabaseHelper(this)

        usersListView = findViewById(R.id.users_list)
        coursesListView = findViewById(R.id.courses_list)
        val courseName: EditText = findViewById(R.id.course_name)
        val courseDescription: EditText = findViewById(R.id.course_description)
        val buttonAddCourse: Button = findViewById(R.id.button_add_course)

        // Load users and courses into the ListView
        loadUsers()
        loadCourses()

        buttonAddCourse.setOnClickListener {
            val name = courseName.text.toString().trim()
            val description = courseDescription.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val course = Course(id = 0, name = name, description = description)
                dbHelper.addCourse(course)
                Toast.makeText(this, "Курс добавлен", Toast.LENGTH_LONG).show()

                // Clear the input fields
                courseName.text.clear()
                courseDescription.text.clear()

                // Reload courses
                loadCourses()
            }
        }

        coursesListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedCourse = courses[position]
            editCourse(selectedCourse)
        }
    }

    private fun loadUsers() {
        users = dbHelper.getAllUsers()
        usersAdapter = UserAdapter(this, users)
        usersListView.adapter = usersAdapter
    }

    private fun loadCourses() {
        courses = dbHelper.getAllCourses()
        val courseNames = courses.map { it.name }
        coursesAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, courseNames)
        coursesListView.adapter = coursesAdapter
    }

    private fun editCourse(course: Course) {
        // Logic to edit a course
        val courseName: EditText = findViewById(R.id.course_name)
        val courseDescription: EditText = findViewById(R.id.course_description)

        courseName.setText(course.name)
        courseDescription.setText(course.description)

        val buttonEditCourse: Button = findViewById(R.id.button_add_course)
        buttonEditCourse.text = "Сохранить изменения"

        buttonEditCourse.setOnClickListener {
            val newName = courseName.text.toString().trim()
            val newDescription = courseDescription.text.toString().trim()

            if (newName.isEmpty() || newDescription.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val updatedCourse = Course(id = course.id, name = newName, description = newDescription)
                dbHelper.updateCourse(updatedCourse)
                Toast.makeText(this, "Курс обновлен", Toast.LENGTH_LONG).show()

                // Clear the input fields
                courseName.text.clear()
                courseDescription.text.clear()

                // Reset button text
                buttonEditCourse.text = "Добавить курс"

                // Reload courses
                loadCourses()
            }
        }
    }
}
