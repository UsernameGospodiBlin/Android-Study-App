package com.example.myapplication
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class UserAdapter(context: Context, users: List<User>) : ArrayAdapter<User>(context, 0, users) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.user_item, parent, false)
        val user = getItem(position)
        val userName = view.findViewById<TextView>(R.id.user_name)
        val userEmail = view.findViewById<TextView>(R.id.user_email)

        userName.text = user?.login
        userEmail.text = user?.email

        return view
    }
}
