package com.example.myapplication

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "app.db"
        private const val DATABASE_VERSION = 5 // Увеличим версию базы данных

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_LOGIN = "login"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
        private const val COLUMN_USER_ROLE = "role"

        private const val TABLE_COURSES = "courses"
        private const val COLUMN_COURSE_ID = "id"
        private const val COLUMN_COURSE_NAME = "name"
        private const val COLUMN_COURSE_DESCRIPTION = "description"

        private const val TABLE_REVIEWS = "reviews"
        private const val COLUMN_REVIEW_ID = "id"
        private const val COLUMN_REVIEW_USER_ID = "user_id"
        private const val COLUMN_REVIEW_COURSE_ID = "course_id"
        private const val COLUMN_REVIEW_TEXT = "review_text"

        private const val TABLE_TASKS = "tasks"
        private const val COLUMN_TASK_ID = "id"
        private const val COLUMN_TASK_NAME = "name"
        private const val COLUMN_TASK_DESCRIPTION = "description"
        private const val COLUMN_TASK_STATUS = "status"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_LOGIN + " TEXT,"
                + COLUMN_USER_EMAIL + " TEXT,"
                + COLUMN_USER_PASSWORD + " TEXT,"
                + COLUMN_USER_ROLE + " TEXT DEFAULT 'user'" + ")")


        val createCoursesTable = ("CREATE TABLE " + TABLE_COURSES + "("
                + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_COURSE_NAME + " TEXT,"
                + COLUMN_COURSE_DESCRIPTION + " TEXT" + ")")

        val createReviewsTable = ("CREATE TABLE " + TABLE_REVIEWS + "("
                + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_REVIEW_USER_ID + " INTEGER,"
                + COLUMN_REVIEW_COURSE_ID + " INTEGER,"
                + COLUMN_REVIEW_TEXT + " TEXT,"
                + "FOREIGN KEY($COLUMN_REVIEW_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID), "
                + "FOREIGN KEY($COLUMN_REVIEW_COURSE_ID) REFERENCES $TABLE_COURSES($COLUMN_COURSE_ID)" + ")")

        val createTasksTable = ("CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TASK_NAME + " TEXT,"
                + COLUMN_TASK_DESCRIPTION + " TEXT,"
                + COLUMN_TASK_STATUS + " TEXT" + ")")

        db?.execSQL(createUsersTable)
        db?.execSQL(createCoursesTable)
        db?.execSQL(createReviewsTable)
        db?.execSQL(createTasksTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db?.execSQL("ALTER TABLE $TABLE_USERS ADD COLUMN $COLUMN_USER_ROLE TEXT DEFAULT 'user'")
        }
        if (oldVersion < 3) {
            db?.execSQL("CREATE TABLE " + TABLE_COURSES + "("
                    + COLUMN_COURSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COURSE_NAME + " TEXT,"
                    + COLUMN_COURSE_DESCRIPTION + " TEXT" + ")")
        }
        if (oldVersion < 4) {
            db?.execSQL("CREATE TABLE " + TABLE_REVIEWS + "("
                    + COLUMN_REVIEW_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_REVIEW_USER_ID + " INTEGER,"
                    + COLUMN_REVIEW_COURSE_ID + " INTEGER,"
                    + COLUMN_REVIEW_TEXT + " TEXT,"
                    + "FOREIGN KEY($COLUMN_REVIEW_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID), "
                    + "FOREIGN KEY($COLUMN_REVIEW_COURSE_ID) REFERENCES $TABLE_COURSES($COLUMN_COURSE_ID)" + ")")
            db?.execSQL("CREATE TABLE " + TABLE_TASKS + "("
                    + COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TASK_NAME + " TEXT,"
                    + COLUMN_TASK_DESCRIPTION + " TEXT,"
                    + COLUMN_TASK_STATUS + " TEXT" + ")")
        }
    }

    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_LOGIN, user.login)
            put(COLUMN_USER_EMAIL, user.email)
            put(COLUMN_USER_PASSWORD, user.password)
            put(COLUMN_USER_ROLE, user.role)
        }
        db.insert(TABLE_USERS, null, values)
        db.close()
    }

    fun getUser(login: String, password: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID, COLUMN_USER_LOGIN, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_ROLE),
            "$COLUMN_USER_LOGIN = ? AND $COLUMN_USER_PASSWORD = ?", arrayOf(login, password),
            null, null, null, null)

        cursor?.moveToFirst()
        if (cursor != null && cursor.count > 0) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                login = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
            )
            cursor.close()
            return user
        }
        cursor?.close()
        return null
    }

    fun updateUserRole(userId: Int, newRole: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ROLE, newRole)
        }
        db.update(TABLE_USERS, values, "$COLUMN_USER_ID = ?", arrayOf(userId.toString()))
        db.close()
    }

    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS", null)
        if (cursor.moveToFirst()) {
            do {
                val user = User(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                    login = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)),
                    email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                    password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                    role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
                )
                users.add(user)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return users
    }

    fun getUserByRole(role: String): User? {
        val db = this.readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USER_ID, COLUMN_USER_LOGIN, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_USER_ROLE),
            "$COLUMN_USER_ROLE = ?", arrayOf(role),
            null, null, null, null)

        cursor?.moveToFirst()
        if (cursor != null && cursor.count > 0) {
            val user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                login = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_LOGIN)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE))
            )
            cursor.close()
            return user
        }
        cursor?.close()
        return null
    }

    fun createAdminUser() {
        // Check if an admin user already exists
        val adminUser = getUserByRole("admin")
        if (adminUser == null) {
            // Create default admin user
            val admin = User(id = 0, login = "admin", email = "admin@example.com", password = "admin", role = "admin")
            addUser(admin)
        }
    }

    // Methods for courses
    fun addCourse(course: Course) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_NAME, course.name)
            put(COLUMN_COURSE_DESCRIPTION, course.description)
        }
        db.insert(TABLE_COURSES, null, values)
        db.close()
    }

    fun getAllCourses(): List<Course> {
        val courses = mutableListOf<Course>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES", null)
        if (cursor.moveToFirst()) {
            do {
                val course = Course(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESCRIPTION))
                )
                courses.add(course)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courses
    }

    fun deleteCourse(courseId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_COURSES, "$COLUMN_COURSE_ID = ?", arrayOf(courseId.toString()))
        db.close()
    }

    // Methods for reviews
    fun addReview(review: Review) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_REVIEW_USER_ID, review.userId)
            put(COLUMN_REVIEW_COURSE_ID, review.courseId)
            put(COLUMN_REVIEW_TEXT, review.reviewText)
        }
        db.insert(TABLE_REVIEWS, null, values)
        db.close()
    }

    fun getAllReviews(): List<Review> {
        val reviews = mutableListOf<Review>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_REVIEWS", null)
        if (cursor.moveToFirst()) {
            do {
                val review = Review(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_ID)),
                    userId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_USER_ID)),
                    courseId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_COURSE_ID)),
                    reviewText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REVIEW_TEXT))
                )
                reviews.add(review)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return reviews
    }

    fun deleteReview(reviewId: Int) {
        val db = this.writableDatabase
        db.delete(TABLE_REVIEWS, "$COLUMN_REVIEW_ID = ?", arrayOf(reviewId.toString()))
        db.close()
    }

    // Methods for tasks
    fun addTask(task: Task) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TASK_NAME, task.name)
            put(COLUMN_TASK_DESCRIPTION, task.description)
            put(COLUMN_TASK_STATUS, task.status)
        }
        db.insert(TABLE_TASKS, null, values)
        db.close()
    }

    fun getAllTasks(): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TASKS", null)
        if (cursor.moveToFirst()) {
            do {
                val task = Task(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_NAME)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_DESCRIPTION)),
                    status = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TASK_STATUS))
                )
                tasks.add(task)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return tasks
    }
    fun updateCourse(course: Course) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_NAME, course.name)
            put(COLUMN_COURSE_DESCRIPTION, course.description)
        }
        db.update(TABLE_COURSES, values, "$COLUMN_COURSE_ID = ?", arrayOf(course.id.toString()))
        db.close()
    }

}
