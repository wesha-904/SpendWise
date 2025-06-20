package com.example.spendwise

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// --- Budget ---
fun saveUserBudget(context: Context, username: String, budget: Float) {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    prefs.edit().putFloat("${username}_budget", budget).apply()
}

fun getUserBudget(context: Context, username: String): Float {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    return prefs.getFloat("${username}_budget", 0f)
}

// --- Expenses ---
fun saveUserExpenses(context: Context, username: String, expenses: List<Transaction>) {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val gson = Gson()
    val expensesJson = gson.toJson(expenses)
    prefs.edit().putString("${username}_expenses", expensesJson).apply()
}

fun getUserExpenses(context: Context, username: String): List<Transaction> {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = prefs.getString("${username}_expenses", "[]")
    val type = object : TypeToken<List<Transaction>>() {}.type
    return gson.fromJson(json, type) ?: emptyList()
}

// --- Incomes ---
fun saveUserIncomes(context: Context, username: String, incomes: List<Transaction>) {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val gson = Gson()
    val incomesJson = gson.toJson(incomes)
    prefs.edit().putString("${username}_incomes", incomesJson).apply()
}

fun getUserIncomes(context: Context, username: String): List<Transaction> {
    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
    val gson = Gson()
    val json = prefs.getString("${username}_incomes", "[]")
    val type = object : TypeToken<List<Transaction>>() {}.type
    return gson.fromJson(json, type) ?: emptyList()
}
