package com.example.spendwise

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class User(
    val username: String,
    val password: String
)

fun saveUserToPrefs(context: Context, username: String, password: String): Boolean {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val usersJson = prefs.getString("users", null)

    val gson = Gson()
    val type = object : TypeToken<MutableList<User>>() {}.type
    val users: MutableList<User> = if (usersJson.isNullOrEmpty()) {
        mutableListOf()
    } else {
        gson.fromJson(usersJson, type)
    }

    // Check if username already exists, return false if it does
    if (users.any { it.username == username }) return false

    users.add(User(username, password))
    prefs.edit().apply {
        putString("users", gson.toJson(users))
        putBoolean("is_logged_in", true)
        putString("current_user", username)
        apply()
    }
    return true
}

fun authenticateUser(context: Context, username: String, password: String): Boolean {
    val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val usersJson = prefs.getString("users", null)

    if (usersJson.isNullOrEmpty()) return false

    val gson = Gson()
    val type = object : TypeToken<List<User>>() {}.type
    val users: List<User> = gson.fromJson(usersJson, type)

    return users.any { it.username == username && it.password == password }
}
