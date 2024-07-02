package com.example.myapplication



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userLogin: EditText = findViewById(R.id.login_login)
        val userPassword: EditText = findViewById(R.id.login_password)
        val button: Button = findViewById(R.id.button_login)

        val dbHelper = DatabaseHelper(this)

        button.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPassword.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show()
            } else {
                val user = dbHelper.getUser(login, pass)
                if (user != null) {
                    Toast.makeText(this, "Вход успешен", Toast.LENGTH_LONG).show()

                    val userRole = user.role
                    when (userRole) {
                        "admin" -> startActivity(Intent(this, AdminActivity::class.java))
                        "teacher" -> startActivity(Intent(this, TeacherActivity::class.java))
                        "user" -> startActivity(Intent(this, UserActivity::class.java))
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Неверные логин или пароль", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
