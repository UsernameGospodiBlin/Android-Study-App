package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class CourseAdapter(private val context: Context, private val courses: List<Course>) : BaseAdapter() {

    override fun getCount(): Int = courses.size

    override fun getItem(position: Int): Any = courses[position]

    override fun getItemId(position: Int): Long = courses[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.course_item, parent, false)

        val course = courses[position]
        val courseNameTextView: TextView = view.findViewById(R.id.course_name)
        val courseDescriptionTextView: TextView = view.findViewById(R.id.course_description)

        courseNameTextView.text = course.name
        courseDescriptionTextView.text = course.description

        return view
    }
}
