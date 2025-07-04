package com.example.mint.models

import java.util.Date

data class ExpenseItem(
    val amount: Double,
    val category: String,
    val description: String,
    val expenseName: String,
    val date: Date,
    val imageUri: String?
)
