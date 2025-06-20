package com.example.spendwise

data class Transaction(
    val type: String,
    val amount: Double,
    val label: String,
    val date: String,
    val note: String = ""
)
